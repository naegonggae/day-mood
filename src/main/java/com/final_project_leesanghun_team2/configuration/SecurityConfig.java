package com.final_project_leesanghun_team2.configuration;

import com.final_project_leesanghun_team2.security.exception.JwtAccessDeniedHandler;
import com.final_project_leesanghun_team2.security.exception.JwtAuthenticationEntryPoint;
import com.final_project_leesanghun_team2.security.jwt.JwtAuthorizationFilter;
import com.final_project_leesanghun_team2.security.exception.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 기본로그인을 사용하지 않음
                .httpBasic().disable()

                // cors 필터 미적용
                // .cors().and()

                .authorizeRequests()
//                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll()
//                .antMatchers(HttpMethod.GET, "/**").permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll().and()


                // csrf 토큰을 생성하지 않고 httpOnly 조건과 https 통신을 통해서 csrf 공격에 대비함
                .csrf((csrf) -> csrf.disable())

                // 세션을 이용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))

                // 에러 핸들링
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler).and();

                // 인증필터 적용
//                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                // 인증과정에서 발생한 에러 핸들링
//                .addFilterBefore(jwtExceptionFilter, JwtAuthorizationFilter.class);

        return http.build();
    }
}
