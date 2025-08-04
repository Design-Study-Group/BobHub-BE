package com.bobhub.mapper;

import com.bobhub.domain.Party;
import com.bobhub.dto.PartyViewResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PartyMapper {
  List<PartyViewResponse> getPartiesByCategory(@Param("category") String category);

  void createParty(Party party);

  Party getPartyById(@Param("id") long id);

  void updateParty(Party party);

  void deleteParty(@Param("id") long id);

  boolean isPartyOwner(@Param("partyId") long partyId, @Param("userId") long userId);
  List<Party> getAllPartyByOwnerId(@Param("id") long id);
}
