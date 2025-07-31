package com.bobhub.service;

import com.bobhub.domain.Comments;

import java.util.List;

public interface CommentService {
    List<Comments> getComments(Long partyId);
    void addComment(Comments comment);
    void updateComment(Comments comment);
    void deleteComment(Comments comments);
}
