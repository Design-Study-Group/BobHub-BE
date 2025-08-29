package com.bobhub.auth.service;

import com.bobhub._core.jwt.JwtTokenProvider;
import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.auth.dto.GoogleOAuthToken;
import com.bobhub.auth.dto.LoginResponse;
import com.bobhub.user.domain.User;
import com.bobhub.user.mapper.UserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

  private static final Logger log = LoggerFactory.getLogger(AuthService.class);

  private final UserMapper userMapper;
  private final JwtTokenProvider jwtTokenProvider;
  private final GoogleIdTokenVerifier googleIdTokenVerifier;
  private final TokenBlacklistService tokenBlacklistService;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String googleClientId;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String googleClientSecret;

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String googleRedirectUri;

  @Transactional
  public LoginResponse loginWithGoogle(String code) throws GeneralSecurityException, IOException {
    String idTokenString = getIdTokenFromGoogle(code);
    GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
    if (idToken == null) {
      throw new IllegalArgumentException("Invalid ID token");
    }
    GoogleIdToken.Payload payload = idToken.getPayload();
    String email = payload.getEmail();
    User user = userMapper.findByEmail(email);
    if (user == null) {
      user =
          User.builder()
              .email(email)
              .name((String) payload.get("name"))
              .picture((String) payload.get("picture"))
              .build();
      userMapper.insertUser(user);
      user = userMapper.findByEmail(email);
    }
    PrincipalDetails principalDetails = new PrincipalDetails(user);
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            principalDetails, null, principalDetails.getAuthorities());
    String accessToken = jwtTokenProvider.generateToken(authentication);
    String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
    return new LoginResponse(accessToken, refreshToken, user);
  }

  public void logout(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return;
    }

    Arrays.stream(cookies)
        .filter(
            cookie ->
                "accessToken".equals(cookie.getName()) || "refreshToken".equals(cookie.getName()))
        .map(Cookie::getValue)
        .filter(StringUtils::hasText)
        .forEach(tokenBlacklistService::addToBlacklist);
  }

  private String getIdTokenFromGoogle(String code) throws JsonProcessingException {
    log.info("Backend redirect_uri for Google token request: {}", googleRedirectUri);
    RestTemplate restTemplate = new RestTemplate();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("client_id", googleClientId);
    params.add("client_secret", googleClientSecret);
    params.add("redirect_uri", googleRedirectUri);
    params.add("grant_type", "authorization_code");
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/x-www-form-urlencoded");
    HttpEntity<MultiValueMap<String, String>> httpRequest = new HttpEntity<>(params, headers);
    ResponseEntity<String> response =
        restTemplate.postForEntity(
            "https://oauth2.googleapis.com/token", httpRequest, String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    GoogleOAuthToken googleOAuthToken =
        objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
    return googleOAuthToken.getIdToken();
  }
}
