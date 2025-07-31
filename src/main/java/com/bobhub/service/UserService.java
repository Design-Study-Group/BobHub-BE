package com.bobhub.service;

import com.bobhub.domain.User;
import com.bobhub.mapper.UserMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("해당 이메일로 사용자를 찾을 수 없습니다: " + email);
        }
        return user;
    }

    public void saveUser(User user) {
        userMapper.insertUser(user);
    }
}
