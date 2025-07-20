package com.bobhub.mapper;

import com.bobhub.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByEmail(String email);
    void insertUser(User user);
}

