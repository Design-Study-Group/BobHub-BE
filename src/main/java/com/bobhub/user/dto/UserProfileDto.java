package com.bobhub.user.dto;

import com.bobhub.user.domain.User;
import lombok.Getter;

@Getter
public class UserProfileDto {
  private final long id;
  private final String name;
  private final String email;
  private final String picture;

  public UserProfileDto(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }
}
