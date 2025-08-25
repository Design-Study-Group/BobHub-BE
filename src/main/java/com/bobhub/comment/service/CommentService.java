package com.bobhub.comment.service;

import com.bobhub.comment.domain.Comments;
import java.util.List;

public interface CommentService {
  List<Comments> getComments(Long partyId);

  void addComment(Comments comment);

  void updateComment(Comments comment);

  void deleteComment(Comments comments);
}
