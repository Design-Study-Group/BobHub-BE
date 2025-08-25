package com.bobhub.comment.service;

import com.bobhub.comment.domain.Comments;
import com.bobhub.comment.mapper.CommentMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

  private final CommentMapper commentMapper;

  public CommentServiceImpl(CommentMapper commentMapper) {
    this.commentMapper = commentMapper;
  }

  @Override
  public List<Comments> getComments(Long partyId) {
    return commentMapper.findByPartyId(partyId);
  }

  @Override
  public void addComment(Comments comment) {
    commentMapper.insertComment(comment);
  }

  @Override
  public void updateComment(Comments comment) {
    commentMapper.updateComment(comment);
  }

  @Override
  public void deleteComment(Comments comment) {
    commentMapper.deleteComment(comment);
  }
}
