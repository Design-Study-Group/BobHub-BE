package com.bobhub.mapper;

import com.bobhub.domain.Party;
import com.bobhub.dto.PartyViewResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartyMapper {
    List<PartyViewResponse> getPartiesByCategory(@Param("category") String category);
    void createParty(Party party);
}
