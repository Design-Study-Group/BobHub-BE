package com.bobhub._core.config;

import com.bobhub._core.jwt.JwtAuthenticationFilter;
import com.bobhub._core.jwt.JwtTokenProvider;
import com.bobhub.auth.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenBlacklistService tokenBlacklistService; // 블랙리스트 서비스 주입

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // CSRF, form login, HTTP basic 인증 비활성화
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // 세션 관리를 STATELESS(상태 없음)으로 설정
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 요청 경로별 인가 설정
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/",
                        "/login",
                        "/error",
                        "/api/oauth/**", // Google ID 토큰 로그인을 위한 새 엔드포인트
                        "/api/actuator/prometheus", // Prometheus 메트릭 수집 엔드포인트
                        "/document/**",
                        "/css/**",
                        "/images/**",
                        "/js/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())

        // 직접 만든 JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
        // 필터 생성 시 tokenBlacklistService를 전달
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenProvider, tokenBlacklistService),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
