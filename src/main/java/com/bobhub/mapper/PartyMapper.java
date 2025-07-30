package com.bobhub.mapper;

import com.bobhub.domain.Party;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartyMapper {
    List<Party> getPartiesByCategory(@Param("category") String category);
}
