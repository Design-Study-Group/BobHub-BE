package com.bobhub.auth.controller;

import com.bobhub.auth.dto.LoginResponse;
import com.bobhub.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    ResponseCookie cookie =
        ResponseCookie.from(name, value)
            .path("/")
            .maxAge(maxAge)
            .httpOnly(true)
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  @PostMapping("/oauth/google")
  public ResponseEntity<?> googleLogin(
      @RequestBody Map<String, String> requestBody, HttpServletResponse response) {
    try {
      String code = requestBody.get("code");
      LoginResponse loginResponse = authService.loginWithGoogle(code);

      // 1. AccessToken을 HttpOnly 쿠키에 설정
      addCookie(response, "accessToken", loginResponse.getAccessToken(), 60 * 60 * 24); // 1일

      // 2. RefreshToken을 HttpOnly 쿠키에 설정
      addCookie(response, "refreshToken", loginResponse.getRefreshToken(), 60 * 60 * 24 * 7); // 7일

      // 3. 응답 본문에는 사용자 정보만 전달
      return ResponseEntity.ok(loginResponse.getUserInfo());

    } catch (Exception e) {
      // 실제 프로덕션에서는 에러를 더 상세히 처리해야 합니다.
      return ResponseEntity.status(401).body("Login Failed: " + e.getMessage());
    }
  }

  @PostMapping("/auth/logout")
  public ResponseEntity<Void> logout(HttpServletRequest req, HttpServletResponse res) {
    // 1. 서버 측에서 Access/Refresh 토큰을 블랙리스트에 추가하여 무효화
    authService.logout(req);

    // 2. 클라이언트 브라우저의 쿠키를 삭제하기 위해 Max-Age=0으로 설정한 쿠키를 응답에 추가
    addCookie(res, "accessToken", null, 0);
    addCookie(res, "refreshToken", null, 0);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
    try {
      String newAccessToken = authService.refreshAccessToken(request);
      addCookie(response, "accessToken", newAccessToken, 60 * 60 * 24); // 1일
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body("Unable to refresh token: " + e.getMessage());
    }
  }
}
