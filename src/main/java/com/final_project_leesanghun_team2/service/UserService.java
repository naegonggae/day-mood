package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.user.Top5JoinUserListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5UserHasMostFollowingListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5UsersHasMostPostsListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateResponse;
import com.final_project_leesanghun_team2.exception.user.DuplicateNickNameException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.FollowRepository;
import com.final_project_leesanghun_team2.repository.post.PostRepository;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import com.final_project_leesanghun_team2.domain.dto.user.TokenResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.request.UserUpdateRequest;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.dto.user.request.UserJoinRequest;
import com.final_project_leesanghun_team2.domain.dto.user.request.UserLoginRequest;
import com.final_project_leesanghun_team2.domain.dto.user.UserJoinResponse;
import com.final_project_leesanghun_team2.exception.user.DuplicateUsernameException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Qualifier("refreshTokenRedisTemplate")
    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenUtil jwtTokenUtil;
    private final CacheEvictService cacheEvictService;

    // 회원가입
    @Transactional
    public UserJoinResponse join(UserJoinRequest request){

        // 이메일 중복 체크
        checkDuplicateUsername(request);

        // 닉네임 중복 체크
        checkDuplicationNickname(request.getNickName());

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(request.getPassword());

        // User 생성
        User user = User.createUser(request, encodedPassword);
        User savedUser = userRepository.save(user);

        // 회원이 새로 가입하면 새로 조인한 top5 유저 캐시 삭제
        cacheEvictService.evictTop5JoinUserList();

        return UserJoinResponse.from(savedUser);
    }

    // 로그인
    public TokenResponse login(UserLoginRequest request) {

        // 로그인정보로 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // 실제 인증 - DaoAuthenticationProvider class 내 additionalAuthenticationChecks() 메소드로 비밀번호 체크
        log.info("login - authenticationManagerBuilder 로 실제 인증을 시작합니다.");
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // 저장되어있던 PrincipalDetails 정보 추출
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 토큰 생성
        TokenResponse tokenResponse = jwtTokenUtil.createToken(principalDetails);
        log.info("토큰 생성 완료");

        // Redis 에 RefreshToke 저장
        refreshTokenRedisTemplate.opsForValue()
                .set(authentication.getName(),
                tokenResponse.getRefreshToken(), jwtTokenUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS); // REFRESH_TOKEN_EXPIRE_TIME 동안 저장 후 삭제
        log.info("{}의 RefreshToke 이 Redis 에 저장되었습니다.", request.getUsername());

        return tokenResponse;
    }

    // 회원단건 조회
    public UserFindResponse findUser(Long userId, Long loginUserId) {

        // 조회하려는 유저
        User findUser = userRepository.findById(userId)
                .orElseThrow(NoSuchUserException::new);

        // 로그인한 유저
        User loginUser = userRepository.findById(loginUserId)
                .orElseThrow(NoSuchUserException::new);
        
        // 조회하려는 유저의 팔로우, 팔로잉 개수
        Long followCount = followRepository.countByFollowUser(findUser);
        Long followingCount = followRepository.countByFollowingUser(findUser);

        // 로그인한 유저가 조회하려는 유저를 팔로우하는지 여부
        boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, loginUser);

        // 조회하려는 유저의 총 게시물 개수
        Long postCount = postRepository.countAllByUser(findUser);
        
        return UserFindResponse.of(findUser, followCount, followingCount, isFollow, postCount);
    }
    
    // 내정보 조회
    public UserFindResponse findLoginUser(User user) {

        // 로그인한 유저
        User loginUser = userRepository.findById(user.getId())
                .orElseThrow(NoSuchUserException::new);

        // 로그인한 유저의 팔로우, 팔로잉 개수
        Long followCount = followRepository.countByFollowUser(loginUser);
        Long followingCount = followRepository.countByFollowingUser(loginUser);

        // 로그인한 유저의 총 게시물 개수
        Long postCount = postRepository.countAllByUser(loginUser);

        return UserFindResponse.of(loginUser, followCount, followingCount, false, postCount);
    }

    // NickName 으로 회원검색
    public UserFindResponse findUserByNickName(String NickName, User user) {

        // 해당 닉네임을 사용하는 유저
        User findUser = userRepository.findByNickName(NickName).orElseThrow(NoSuchUserException::new);

        // 로그인한 유저
        User loginUser = userRepository.findById(user.getId()).orElseThrow(NoSuchUserException::new);

        // 유저의 팔로우, 팔로잉 개수
        Long followCount = followRepository.countByFollowUser(findUser);
        Long followingCount = followRepository.countByFollowingUser(findUser);

        // 로그인한 유저가 해당 닉네임을 사용하는 유저를 팔로우하는지 여부
        boolean isFollow = followRepository.existsByFollowUserAndFollowingUser(findUser, loginUser);

        // 유저의 총 게시물 개수
        Long postCount = postRepository.countAllByUser(findUser);

        return UserFindResponse.of(findUser, followCount, followingCount, isFollow, postCount);
    }

    // 새로 조인한 유저 top5 조회
    @Cacheable(cacheNames = "top5JoinUserList")
    public List<Top5JoinUserListResponse> top5JoinUserList() {
        List<User> userList = userRepository.findTop5ByOrderByCreatedAtDesc();
        return userList.stream()
                .map(user -> Top5JoinUserListResponse.of(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 활동이 가장 많은 유저 5명 조회
    @Cacheable(cacheNames = "top5UserHasMostPostsList")
    public List<Top5UsersHasMostPostsListResponse> top5UserHasMostPostsList() {
        List<User> userList = postRepository.findTop5UsersWithMostPosts();
        return userList.stream()
                .map(user -> Top5UsersHasMostPostsListResponse.of(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 팔로우를 가장 많이 받은 유저 5명 조회
    @Cacheable(cacheNames = "top5UserHasMostFollowingList")
    public List<Top5UserHasMostFollowingListResponse> top5UserHasMostFollowingList() {
        List<User> userList = followRepository.findTop5UsersWithMostFollowers();
        return userList.stream()
                .map(user -> Top5UserHasMostFollowingListResponse.of(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 랭킹 캐시 삭제 & 갱신 스케줄링
    @Scheduled(cron = "0 0 * * * *") // 매 시간 0분 0초에 스케줄링
    public void scheduleTop5UserListEvictAndCache() {
        // top5UserHasMostPostsList 캐시 삭제 후 갱신
        cacheEvictService.evictTop5UserHasMostPostsList();
        top5UserHasMostPostsList();

        // top5UserHasMostFollowingList 캐시 삭제 후 갱신
        cacheEvictService.evictTop5UserHasMostFollowingList();
        top5UserHasMostFollowingList();
    }

    // 회원정보 수정
    @Transactional
    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request, User user) {

        // 변경한 닉네임이 중복되는 닉네임인지 확인
        checkDuplicationNickname(request.getNickName());

        // 로그인한 유저로 바로 수정할 수 없음. -> 영속성에 가져와야 변경감지를 통해 수정하기 때문
        User findUser = userRepository.findById(id).orElseThrow(NoSuchUserException::new);

        // 조회한 유저와 로그인한 유저가 같을 때 수정을 할 수 있다.
        hasPermission(user, findUser);

        findUser.update(request.getNickName());
        return UserUpdateResponse.from(findUser);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(Long id, User user) {

        // 로그인한 유저를 영속성에 가져옴
        User findUser = userRepository.findById(id).orElseThrow(NoSuchUserException::new);

        // 조회한 유저와 로그인한 유저가 같을 때 삭제를 할 수 있다.
        hasPermission(user, findUser);

        // 태그포스트 게시물, 댓글, 팔로우, 좋아요 지우고 user 삭제한다.
        userRepository.delete(findUser);
    }

    private void checkDuplicationNickname(String nickName) {
        boolean findNickName = userRepository.existsByNickName(nickName);
        if (findNickName) throw new DuplicateNickNameException();
    }

    private void checkDuplicateUsername(UserJoinRequest request) {
        boolean findUsername = userRepository.existsByUsername(request.getUsername());
        if (findUsername) throw new DuplicateUsernameException();
    }

    private void hasPermission(User user, User findUser) {
        if (!Objects.equals(findUser.getUsername(), user.getUsername())) throw new PermissionDeniedException();
    }
}
