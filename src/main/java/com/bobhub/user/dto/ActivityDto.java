package com.bobhub.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityDto {
  private Long id;
  private String type;
  private String message;
  private String date;
}
