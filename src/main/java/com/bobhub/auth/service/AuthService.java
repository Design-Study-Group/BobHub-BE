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
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Transactional
    public LoginResponse loginWithGoogle(String code) throws GeneralSecurityException, IOException {
        // 1. 코드를 사용하여 구글로부터 ID 토큰을 받음
        String idTokenString = getIdTokenFromGoogle(code);

        // 2. 구글 ID 토큰 확인
        GoogleIdToken idToken = googleIdTokenVerifier.verify(idTokenString);
        if (idToken == null) {
            throw new IllegalArgumentException("Invalid ID token");
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // 3.DB에서 사용자 정보 조회 및 등록
        String email = payload.getEmail();
        User user = userMapper.findByEmail(email);

        if (user == null) {
            // 등록된 정보가 없으면 DB에 사용자 정보 등록
            user = User.builder()
                    .email(email)
                    .name((String) payload.get("name"))
                    .picture((String) payload.get("picture"))
                    .build();
            userMapper.insertUser(user);
            // 사용자 정보 등록 후 등록된 사용자 정보를 가져옴
            user = userMapper.findByEmail(email);
        }

        // 4. 인증 객체 생성
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails,
                null,
                principalDetails.getAuthorities()
        );

        // 5. JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateToken(authentication);

        // 6. 사용자 정보 포함된 토큰 반환
        return new LoginResponse(accessToken, user);
    }

    private String getIdTokenFromGoogle(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken googleOAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);

        return googleOAuthToken.getIdToken();
    }
}
