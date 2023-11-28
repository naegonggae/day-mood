package com.final_project_leesanghun_team2.configuration;

import com.final_project_leesanghun_team2.repository.UserRepository;
import com.final_project_leesanghun_team2.security.exception.JwtAccessDeniedHandler;
import com.final_project_leesanghun_team2.security.exception.JwtAuthenticationEntryPoint;
import com.final_project_leesanghun_team2.security.jwt.JwtAuthorizationFilter;
import com.final_project_leesanghun_team2.security.exception.JwtExceptionFilter;
import com.final_project_leesanghun_team2.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // 기본로그인을 사용하지 않음
                .httpBasic().disable()
//                .cors().and()
                .authorizeRequests()
                .antMatchers("/**").permitAll().and()
//                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll() // join, login은 언제나 가능
//                .antMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
//                .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
//                .and()
                // csrf 토큰을 생성하지 않고 httpOnly 조건과 https 통신을 통해서 csrf 공격에 대비함
                .csrf((csrf) -> csrf.disable())
                // 세션을 이용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                // 에러 핸들링
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                // 인증과정에서 발생한 에러 핸들링
                .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class)
                // 인증필터 적용
                .apply(new MyCustomDsl())
                .and()
                .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthorizationFilter atr = new JwtAuthorizationFilter(authenticationManager, userRepository, jwtTokenUtil);
            http
                    .addFilterBefore(atr, BasicAuthenticationFilter.class);
        }
    }
}
