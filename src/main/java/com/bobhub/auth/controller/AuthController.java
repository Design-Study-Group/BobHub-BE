package com.bobhub.auth.controller;

import com.bobhub.auth.dto.GoogleLoginRequest;
import com.bobhub.auth.dto.LoginResponse;
import com.bobhub.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/oauth/google")
  public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request)
      throws GeneralSecurityException, IOException {
    System.out.println("로그인 요청 들어옴");
    LoginResponse loginResponse = authService.loginWithGoogle(request.getCode());
    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/auth/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    authService.logout(request);
    return ResponseEntity.ok().build();
  }
}
