package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateResponse;
import com.final_project_leesanghun_team2.exception.user.DuplicateNickNameException;
import com.final_project_leesanghun_team2.exception.user.PermissionDeniedException;
import com.final_project_leesanghun_team2.security.domain.PrincipalDetails;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import com.final_project_leesanghun_team2.domain.dto.user.TokenResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserFindResponse;
import com.final_project_leesanghun_team2.domain.dto.user.UserUpdateRequest;
import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.dto.user.UserJoinRequest;
import com.final_project_leesanghun_team2.domain.dto.user.UserLoginRequest;
import com.final_project_leesanghun_team2.domain.dto.user.UserJoinResponse;
import com.final_project_leesanghun_team2.exception.user.DuplicateUsernameException;
import com.final_project_leesanghun_team2.exception.user.NoSuchUserException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import java.util.Objects;
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
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenUtil jwtTokenUtil;

    // 회원가입
    @Transactional
    public UserJoinResponse join(UserJoinRequest request){

        // 이메일 중복 체크
        boolean findUsername = userRepository.existsByUsername(request.getUsername());
        if (findUsername) throw new DuplicateUsernameException();

        // 닉네임 중복 체크
        boolean findNickName = userRepository.existsByNickName(request.getNickName());
        if (findNickName) throw new DuplicateNickNameException();

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

        return UserFindResponse.from(findUser);
    }


    // NickName 으로 회원검색
    public UserFindResponse findUsername(String NickName) {

        // 해당 닉네임을 가진 회원 검색
        User findUser = userRepository.findByNickName(NickName).orElseThrow(NoSuchUserException::new);

        return UserFindResponse.from(findUser);
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
        log.info(findUser.getUsername());
        log.info(user.getUsername());
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

        // 게시물, 댓글, 팔로우, 좋아요 지우고 user 삭제한다.

        userRepository.delete(findUser);
    }

    // 로그아웃
    // 쿠키에 있는 엑세스 토큰을 뺏는다.

}
