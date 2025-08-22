package com.bobhub.auth.controller;

import com.bobhub.auth.dto.GoogleLoginRequest;
import com.bobhub.auth.dto.LoginResponse;
import com.bobhub.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request) throws GeneralSecurityException, IOException {
        System.out.println("로그인 요청 들어옴");
        LoginResponse loginResponse = authService.loginWithGoogle(request.getCode());
        return ResponseEntity.ok(loginResponse);
    }
}
