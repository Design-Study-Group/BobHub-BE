package com.bobhub.mapper;

import com.bobhub.domain.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comments> findByPartyId(Long partyId);
    void insertComment(Comments comment);
    void updateComment(Comments comment);
    void deleteComment(Comments comment);
}
