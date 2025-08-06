package com.bobhub.mapper;

import com.bobhub.domain.Comments;
import java.util.List;

import com.bobhub.domain.PartyMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartyMemberMapper {
    List<PartyMember> findByUserId(Long userId);
}
