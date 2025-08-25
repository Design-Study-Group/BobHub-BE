package com.bobhub.comment.mapper;

import com.bobhub.comment.domain.Comments;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
  List<Comments> findByPartyId(Long partyId);

  void insertComment(Comments comment);

  void updateComment(Comments comment);

  void deleteComment(Comments comment);
}
