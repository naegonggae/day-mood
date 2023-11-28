package com.final_project_leesanghun_team2.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenUtil jwtTokenUtil;


//    @Value("${jwt.token.secret}")
//    private String secretKey;
//    private Long expireTimeMS = 1000 * 60 * 60l; // 1시간

    // 회원가입
    @Transactional
    public UserJoinResponse join(UserJoinRequest request){

        boolean findUsername = userRepository.existsByUsername(request.getUsername());
        if (findUsername) throw new DuplicateUsernameException();

        String encodedPassword = encoder.encode(request.getPassword());

        User user = User.createUser(request.getUsername(), encodedPassword);
        User savedUser = userRepository.save(user);

        return UserJoinResponse.from(savedUser);
    }

    // 로그인
    public TokenResponse login(UserLoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        System.out.println("memberService-login authenticationManagerBuilder 로 실제 인증을 시작합니다.");
        // 실제 인증 - DaoAuthenticationProvider class 내 additionalAuthenticationChecks() 메소드로 비밀번호 체크
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

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


    // username 으로 회원검색
    public UserFindResponse findUsername(String username) {
        User findUser = userRepository.findByUsername(username).orElseThrow(NoSuchUserException::new);
        return UserFindResponse.from(findUser);
    }

    // 회원정보 수정
    @Transactional
    public void update(UserUpdateRequest request, User user) {

//        User findUser = userRepository.findById(id)
//                .orElseThrow(NoSuchUserException::new);

        // 수정하려면 좀 더 확인해야하지 않을까?
        // 로그인한 사람이 로그인한 유저의 정보만 수정 가능

        user.update(request.getUsername());
    }

    // 회원 삭제
    @Transactional
    public void delete(User user) {

//        User findUser = userRepository.findById(id)
//                .orElseThrow(NoSuchUserException::new);

        // 삭제하려면 좀 더 나 인지 확인해야하지 않을까?
        // 로그인한 사람이 로그인한 유저의 정보만 삭제 가능

        userRepository.deleteById(user.getId());
    }

    // 로그아웃

}
