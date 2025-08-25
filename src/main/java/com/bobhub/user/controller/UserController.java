package com.bobhub.user.controller;

import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.user.dto.ActivityDto;
import com.bobhub.user.dto.UserProfileDto;
import com.bobhub.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/profile")
  public ResponseEntity<UserProfileDto> getUserProfile(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    UserProfileDto userProfile = userService.getUserProfile(principalDetails);
    return ResponseEntity.ok(userProfile);
  }

  @GetMapping("/activity")
  public ResponseEntity<List<ActivityDto>> getUserActivity(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    List<ActivityDto> userActivities = userService.getUserActivity(principalDetails);
    return ResponseEntity.ok(userActivities);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    userService.deleteUser(principalDetails);
    return ResponseEntity.ok().build();
  }
}
