package com.bobhub.activity.service;

import com.bobhub.activity.dto.ActivityResponse;
import com.bobhub.activity.mapper.ActivityMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

  private final ActivityMapper activityMapper;

  public List<ActivityResponse> findRecentActivities() {
    return activityMapper.findRecentActivities();
  }
}
