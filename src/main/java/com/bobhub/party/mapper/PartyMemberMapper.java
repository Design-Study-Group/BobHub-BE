package com.bobhub.party.mapper;

import com.bobhub.party.domain.PartyMember;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PartyMemberMapper {
  List<PartyMember> findByUserId(Long userId);
}
