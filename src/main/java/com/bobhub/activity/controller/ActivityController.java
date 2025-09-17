package com.bobhub.activity.controller;

import com.bobhub.activity.dto.ActivityResponse;
import com.bobhub.activity.service.ActivityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityController {

  private final ActivityService activityService;

  @GetMapping("/recent-activities")
  public List<ActivityResponse> getRecentActivities() {
    return activityService.findRecentActivities();
  }
}
