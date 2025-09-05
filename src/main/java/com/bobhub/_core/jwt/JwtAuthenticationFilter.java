package com.bobhub._core.jwt;

import com.bobhub._core.exception.ErrorCode;
import com.bobhub._core.utils.ApiUtils;
import com.bobhub.auth.service.TokenBlacklistService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";

  private final JwtTokenProvider jwtTokenProvider;
  private final TokenBlacklistService tokenBlacklistService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    String requestURI = httpServletRequest.getRequestURI();

    System.out.println(">>> [JwtAuthenticationFilter] Processing URI: " + requestURI);

    // 인증이 필요 없는 경로는 필터를 즉시 통과시킵니다.
    if (isAuthBypassURI(requestURI)) {
      chain.doFilter(request, response);
      return;
    }

    String jwt = resolveToken(httpServletRequest);

    if (!StringUtils.hasText(jwt)) {
      sendErrorResponse((HttpServletResponse) response, ErrorCode.TOKEN_NOT_FOUND);
      return;
    }

    try {
      jwtTokenProvider.validateToken(jwt);

      if (tokenBlacklistService.isBlacklisted(jwt)) {
        sendErrorResponse((HttpServletResponse) response, ErrorCode.INVALID_TOKEN);
        return;
      }

      Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (ExpiredJwtException e) {
      sendErrorResponse((HttpServletResponse) response, ErrorCode.TOKEN_EXPIRED);
      return;
    } catch (JwtException | IllegalArgumentException e) {
      sendErrorResponse((HttpServletResponse) response, ErrorCode.INVALID_TOKEN);
      return;
    }

    chain.doFilter(request, response);
  }

  private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode)
      throws IOException {
    response.setCharacterEncoding("utf-8");
    response.setStatus(errorCode.getStatus().value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(ApiUtils.error(errorCode)));
  }

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

  private boolean isAuthBypassURI(String uri) {
    return uri.equals("/api/refresh")
        || uri.startsWith("/api/oauth/")
        || uri.startsWith("/api/chatbot/");
  }
}
