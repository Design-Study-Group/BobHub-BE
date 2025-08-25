package com.bobhub.user.mapper;

import com.bobhub.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByEmail(String email);
    User findById(Long id);
    void insertUser(User user);
    void deleteById(Long id);
}
