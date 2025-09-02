package com.bobhub.user.mapper;

import com.bobhub.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
  User findByEmail(String email);

  User findById(Long id);

  void insertUser(User user);

  void updateName(@Param("id") Long id, @Param("name") String name);

  void deleteById(Long id);
}
