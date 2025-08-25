package com.bobhub.user.service;

import com.bobhub._core.security.PrincipalDetails;
import com.bobhub.user.domain.User;
import com.bobhub.user.dto.ActivityDto;
import com.bobhub.user.dto.UserProfileDto;
import com.bobhub.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(PrincipalDetails principalDetails) {
        User user = findUserById(principalDetails.getUser().getId());
        return new UserProfileDto(user);
    }

    @Transactional(readOnly = true)
    public List<ActivityDto> getUserActivity(PrincipalDetails principalDetails) {
        // TODO: 실제 사용자 활동 데이터를 조회하는 로직 구현 필요
        // 현재는 프론트엔드 개발을 위한 예시 데이터를 반환합니다.
        List<ActivityDto> dummyActivities = new ArrayList<>();
        dummyActivities.add(new ActivityDto(1L, "party", "점심 같이 먹을 사람 구해요!", LocalDate.now().minusDays(1).toString()));
        dummyActivities.add(new ActivityDto(2L, "betting", "오늘 점심 내기에서 이겼습니다!", LocalDate.now().minusDays(2).toString()));
        dummyActivities.add(new ActivityDto(3L, "restaurant", "'최고의 국밥' 집에 리뷰를 남겼습니다.", LocalDate.now().minusDays(5).toString()));
        dummyActivities.add(new ActivityDto(4L, "ai", "AI 챗봇에게 메뉴를 추천받았습니다.", LocalDate.now().minusDays(7).toString()));
        dummyActivities.add(new ActivityDto(5L, "party", "저녁 치킨 파티원을 모집합니다.", LocalDate.now().minusDays(10).toString()));

        return dummyActivities;
    }

    @Transactional
    public void deleteUser(PrincipalDetails principalDetails) {
        Long userId = principalDetails.getUser().getId();
        userMapper.deleteById(userId);
    }

    private User findUserById(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("해당 ID로 사용자를 찾을 수 없습니다: " + userId);
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이메일로 사용자를 찾을 수 없습니다: " + email);
        }
        return user;
    }

    @Transactional
    public void saveUser(User user) {
        userMapper.insertUser(user);
    }
}
