package com.bobhub.activity.mapper;

import com.bobhub.activity.dto.ActivityResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityMapper {
  List<ActivityResponse> findRecentActivities();
}
