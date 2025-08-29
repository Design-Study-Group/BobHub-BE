package com.bobhub.auth.dto;

import com.bobhub.user.domain.User;
import lombok.Getter;

@Getter
public class LoginResponse {
  private final String accessToken;
  private final String refreshToken;
  private final UserInfo userInfo;

  public LoginResponse(String accessToken, String refreshToken, User user) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.userInfo = new UserInfo(user);
  }

  @Getter
  private static class UserInfo {
    private final Long id;
    private final String email;
    private final String name;
    private final String picture;

    public UserInfo(User user) {
      this.id = user.getId();
      this.email = user.getEmail();
      this.name = user.getName();
      this.picture = user.getPicture();
    }
  }
}
