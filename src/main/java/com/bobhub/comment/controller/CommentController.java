package com.bobhub.comment.controller;

import com.bobhub.comment.domain.Comments;
import com.bobhub.comment.service.CommentService;
import com.bobhub.user.mapper.UserMapper;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
// @RequestMapping("/api/parties/{partyId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final UserMapper userMapper;

  // 댓글 목록 + 입력 폼
  @GetMapping
  public List<Comments> showComments(@PathVariable Long partyId, Model model) {
    return commentService.getComments(partyId);
  }

  // 댓글 작성
  @PostMapping
  public void addComment(
      @PathVariable Long partyId, @RequestBody Comments comment, Principal principal) {
    Long userId = getUserIdFromPrincipal(principal);

    System.out.println(comment);
    comment.setWriterId(userId);
    comment.setPartyId(partyId);

    commentService.addComment(comment);
  }

  // 댓글 삭제
  @PostMapping("/delete")
  public void deleteComment(@ModelAttribute Comments comment) {
    commentService.deleteComment(comment);
  }

  private Long getUserIdFromPrincipal(Principal principal) {
    if (principal == null) {
      return null;
    }

    String email = null;
    if (principal instanceof OAuth2AuthenticationToken) {
      OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
      email = (String) oauthToken.getPrincipal().getAttributes().get("email");
    } else {
      email = principal.getName();
    }

    if (email == null) {
      return null;
    }

    var user = userMapper.findByEmail(email);
    return user != null ? user.getId() : null;
  }
}
