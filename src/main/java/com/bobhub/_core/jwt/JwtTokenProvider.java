package com.bobhub._core.jwt;

import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

  private final Key key;

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

  public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String generateToken(Authentication authentication) {
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
    User user = principalDetails.getUser();

    long now = (new Date()).getTime();
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

    return Jwts.builder()
        .setSubject(String.valueOf(user.getId()))
        .claim("id", user.getId())
        .claim("email", user.getEmail())
        .setExpiration(accessTokenExpiresIn)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public Authentication getAuthentication(String accessToken) {
    Claims claims = parseClaims(accessToken);

    Collection<? extends GrantedAuthority> authorities =
        Collections.singleton((GrantedAuthority) () -> "ROLE_USER");

    User user =
        User.builder()
            .id(claims.get("id", Long.class))
            .email(claims.get("email", String.class))
            .build();

    PrincipalDetails principal = new PrincipalDetails(user);

    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  /** 토큰의 유효성을 검증합니다. 유효하지 않은 경우 (만료, 서명 오류 등) JwtException 또는 그 하위 예외를 던집니다. */
  public void validateToken(String token) {
    Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }

  private Claims parseClaims(String accessToken) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
  }

  public String generateRefreshToken(Authentication authentication) {
    long now = (new Date()).getTime();
    Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

    String userId = authentication.getName();

    return Jwts.builder()
        .setSubject(userId)
        .setExpiration(refreshTokenExpiresIn)
        .signWith(key, SignatureAlgorithm.HS512)
        .compact();
  }

  public Long getUserIdFromToken(String token) {
    Claims claims = parseClaims(token);
    return Long.parseLong(claims.getSubject());
  }
}
