package com.final_project_leesanghun_team2.configuration;

import com.final_project_leesanghun_team2.service.UserService;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorizaion이 header에 없거나 Bearer 로 시작하지 않는 경우 리턴 // 토큰을 비교하지않고 형식만 비교하는거야
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization:{}", authorization); //authorization이 잘 꺼내졌는지 확인할수있음


        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("Token이 없거나 잘못되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 꺼내기 한칸띄고 첫번째꺼가 토큰이다.
        String token = authorization.split(" ")[1];
        log.info("token:{}", token);

        // expired되었는지 여부
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            log.error("Token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        //ExpiredJwtException 만약 실행했는데 이 에러나면 다른 secretKey로 받은거니까 다시 로그인해서 토큰값 최신화해줘
        // 그리고 한시간 이따가 만료되었다고 나올거야

        // 아래의 코드를 추가해서 문을 열었음
        //UserName token에서 꺼내기
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName:{}", userName);

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER"))); // role은 일단 "USER"로 하드코딩
        // Detail을 넣어줍니다.
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
