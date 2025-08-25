package com.bobhub.comment.controller;

import com.bobhub.comment.domain.Comments;
import com.bobhub.comment.service.CommentService;
import com.bobhub.user.domain.User;
import com.bobhub.user.service.UserService;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/parties/{partyId}/comments")
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;

  public CommentController(CommentService commentService, UserService userService) {
    this.commentService = commentService;
    this.userService = userService;
  }

  // 댓글 목록 + 입력 폼
  @GetMapping
  public String showComments(@PathVariable Long partyId, Model model) {
    List<Comments> comments = commentService.getComments(partyId);
    model.addAttribute("comments", comments);
    model.addAttribute("commentForm", new Comments());
    model.addAttribute("partyId", partyId);

    return "comments/comment-list";
  }

  // 댓글 작성
  @PostMapping
  public String addComment(
      @PathVariable Long partyId,
      @ModelAttribute("commentForm") Comments comment,
      @AuthenticationPrincipal OAuth2User oauthUser) {
    String email = oauthUser.getAttribute("email");
    User user = userService.findByEmail(email);
    comment.setWriterId(user.getId());
    comment.setPartyId(partyId);

    commentService.addComment(comment);
    return "redirect:/parties/" + partyId + "/comments";
  }

  // 댓글 삭제
  @PostMapping("/delete")
  public String deleteComment(@ModelAttribute Comments comment) {
    commentService.deleteComment(comment);
    return "redirect:/parties/comments";
  }
}
