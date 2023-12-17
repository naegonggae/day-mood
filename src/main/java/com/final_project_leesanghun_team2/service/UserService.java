package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.user.Top5NewUserListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5FollowerUserListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.Top5LikeUserListResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateResponse;
import com.final_project_leesanghun_team2.exception.user.DuplicateNickNameException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.repository.CommentRepository;
import com.final_project_leesanghun_team2.repository.FollowRepository;
import com.final_project_leesanghun_team2.repository.LikesRepository;
import com.final_project_leesanghun_team2.repository.PostRepository;
import com.final_project_leesanghun_team2.repository.TagPostRepository;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor @Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final LikesRepository likesRepository;
    private final TagPostRepository tagPostRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenUtil jwtTokenUtil;

    // 회원가입
    @Transactional
    public UserJoinResponse join(UserJoinRequest request){

        // 이메일 중복 체크
        checkDuplicateUsername(request);

        // 닉네임 중복 체크
        checkDuplicationNickname(request);

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(request.getPassword());

        // User 생성
        User user = User.createUser(request, encodedPassword);
        User savedUser = userRepository.save(user);

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

        // 저장되어있던 PrincipalDetails 정보 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 엑세스 토큰 생성
        TokenResponse tokenResponse = jwtTokenUtil.createToken(principalDetails);

        return tokenResponse;
    }

    // 회원단건 조회
    public UserFindResponse findOne(Long id) {

        User findUser = userRepository.findById(id)
                .orElseThrow(NoSuchUserException::new);

        // 유저의 총 게시물 개수 조회
        Long totalPost = postRepository.countAllByUser(findUser);

        return new UserFindResponse(findUser, totalPost);
    }

    // 내정보 조회
    public UserFindResponse findMe(User user) {

        User findUser = userRepository.findById(user.getId())
                .orElseThrow(NoSuchUserException::new);

        // 유저의 총 게시물 개수 조회
        Long totalPost = postRepository.countAllByUser(findUser);

        return new UserFindResponse(findUser, totalPost);
    }

    // NickName 으로 회원검색
    public UserFindResponse findNickName(String NickName) {

        // 해당 닉네임을 가진 회원 검색
        User findUser = userRepository.findByNickName(NickName).orElseThrow(NoSuchUserException::new);

        // 유저의 총 게시물 개수 조회
        Long totalPost = postRepository.countAllByUser(findUser);

        return new UserFindResponse(findUser, totalPost);
    }

    // 새로 조인한 유저 5명 조회
    public List<Top5NewUserListResponse> newUserList() {
        List<User> userList = userRepository.findTop5ByOrderByCreatedAtDesc();
        return userList.stream()
                .map(user -> Top5NewUserListResponse.from(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 좋아요를 가장 많이 받은 유저 5명 조회
    public List<Top5LikeUserListResponse> userLikeList() {
        List<User> userList = userRepository.findTop5ByOrderByTotalLikeDesc();
        return userList.stream()
                .map(user -> Top5LikeUserListResponse.from(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 팔로우를 가장 많이 받은 유저 5명 조회
    public List<Top5FollowerUserListResponse> userFollowList() {
        List<User> userList = userRepository.findTop5ByOrderByFollowingNumDesc();
        return userList.stream()
                .map(user -> Top5FollowerUserListResponse.from(user, userList.indexOf(user)))
                .collect(Collectors.toList());
    }

    // 회원정보 수정
    @Transactional
    public UserUpdateResponse update(Long id, UserUpdateRequest request, User user) {

        // 변경한 닉네임이 중복되는 닉네임인지 확인
        String updateNickName = request.getNickName();

        boolean findNickName = userRepository.existsByNickName(updateNickName);
        if (findNickName) throw new DuplicateNickNameException();

        // 로그인한 유저로 바로 수정할 수 없음. -> 영속성에 가져와야 변경감지를 통해 수정하기 때문
        User findUser = userRepository.findById(id)
                .orElseThrow(NoSuchUserException::new);

        // 조회한 유저와 로그인한 유저가 같을 때 수정을 할 수 있다.
        log.debug(String.valueOf(findUser.getId()));
        log.debug(String.valueOf(user.getId()));
        if (!Objects.equals(findUser.getUsername(), user.getUsername())) throw new PermissionDeniedException();

        findUser.update(request.getNickName());
        return UserUpdateResponse.from(findUser);
    }

    // 회원 삭제
    @Transactional
    public void delete(Long id, User user) {

        User findUser = userRepository.findById(id)
                .orElseThrow(NoSuchUserException::new);

        // 조회한 유저와 로그인한 유저가 같을 때 삭제를 할 수 있다.
        if (!Objects.equals(findUser.getUsername(), user.getUsername())) throw new PermissionDeniedException();

        // 태그포스트 게시물, 댓글, 팔로우, 좋아요 지우고 user 삭제한다.


        userRepository.delete(findUser);
    }

    // 로그아웃
    // js 함수로 쿠키에 있는 엑세스 토큰을 제거한다.

    private void checkDuplicationNickname(UserJoinRequest request) {
        boolean findNickName = userRepository.existsByNickName(request.getNickName());
        if (findNickName) throw new DuplicateNickNameException();
    }

    private void checkDuplicateUsername(UserJoinRequest request) {
        boolean findUsername = userRepository.existsByUsername(request.getUsername());
        if (findUsername) throw new DuplicateUsernameException();
    }
}
