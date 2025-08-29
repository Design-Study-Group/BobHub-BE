package com.bobhub._core.jwt;

import com.bobhub.auth.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenBlacklistService tokenBlacklistService; // 블랙리스트 서비스 주입

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String jwt = resolveToken(httpServletRequest);
    String requestURI = httpServletRequest.getRequestURI();

    // 토큰 유효성 검증 시, 블랙리스트에 있는지도 확인
    if (StringUtils.hasText(jwt)
        && jwtTokenProvider.validateToken(jwt)
        && !tokenBlacklistService.isBlacklisted(jwt)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      logger.info(
          "Security Context에 '" + authentication.getName() + "' 인증 정보를 저장했습니다, uri: " + requestURI);
    } else {
      logger.debug("유효한 JWT 토큰이 없거나 블랙리스트에 등록된 토큰입니다, uri: " + requestURI);
    }

    chain.doFilter(request, response);
  }

  // Extract token from request header
  private String resolveToken(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    return Arrays.stream(cookies)
        .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }
}
