package com.final_project_leesanghun_team2.service;

import com.final_project_leesanghun_team2.domain.entity.User;
import com.final_project_leesanghun_team2.domain.request.UserJoinRequest;
import com.final_project_leesanghun_team2.domain.request.UserLoginRequest;
import com.final_project_leesanghun_team2.domain.response.UserJoinResponse;
import com.final_project_leesanghun_team2.domain.response.UserLoginResponse;
import com.final_project_leesanghun_team2.exception.ErrorCode;
import com.final_project_leesanghun_team2.exception.UserSnsException;
import com.final_project_leesanghun_team2.repository.UserRepository;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private Long expireTimeMS = 1000 * 60 * 60l; // 1시간

    /** 회원가입 **/
    public UserJoinResponse join(UserJoinRequest userJoinRequest){
        // Q: 파라미터 구성을 직관적으로 보이게 짜는게 좋을까? 아니면 객체로 받는게 좋을까?
        // A: 객체로 받고 추가할거 하면될듯.

        // userName 중복체크
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new UserSnsException(ErrorCode.DUPLICATED_USER_NAME);
                });

        // DB에 저장
        String encodedPW = encoder.encode(userJoinRequest.getPassword());
        User savedUser = userRepository.save(User.of(userJoinRequest.getUserName(), encodedPW));

        return UserJoinResponse.of(savedUser);
    }

    /** 로그인 **/
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        // userName 중복체크
        User user = userRepository.findByUserName(userLoginRequest.getUserName())
                .orElseThrow(() -> new UserSnsException(ErrorCode.USERNAME_NOT_FOUND)
            );

        // 로그인할때 받은 비밀번호, DB에 저장된 비밀번호 비교
        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new UserSnsException(ErrorCode.INVALID_PASSWORD);
        }

        // 로그인한 userName, secretKey, expireTimeMS로 토큰만들기
        String token = JwtTokenUtil.createToken(user.getUserName(), secretKey, expireTimeMS);

        return new UserLoginResponse(token);
    }
}
