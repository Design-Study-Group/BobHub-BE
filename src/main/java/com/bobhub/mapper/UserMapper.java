package com.bobhub.mapper;

import com.bobhub.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
  User findByEmail(String email);
  void insertUser(User user);
}
