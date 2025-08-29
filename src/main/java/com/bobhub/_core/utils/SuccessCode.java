package com.bobhub._core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // 200 OK
    OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    GET_RECOMMENDATION(HttpStatus.OK, "게시글 조회에 성공했습니다."),
    GET_RECOMMENDATION_LIST(HttpStatus.OK, "게시글 목록 조회에 성공했습니다."),
    GET_COMMENT_LIST(HttpStatus.OK, "댓글 목록 조회에 성공했습니다."),

    // 201 CREATED
    CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),
    CREATE_RECOMMENDATION(HttpStatus.CREATED, "게시글 생성에 성공했습니다."),
    CREATE_COMMENT(HttpStatus.CREATED, "댓글 생성에 성공했습니다."),

    // 204 NO_CONTENT
    NO_CONTENT(HttpStatus.NO_CONTENT, "요청이 성공적으로 처리되었지만, 반환할 내용이 없습니다."),
    DELETE_RECOMMENDATION(HttpStatus.NO_CONTENT, "게시글 삭제에 성공했습니다."),
    DELETE_COMMENT(HttpStatus.NO_CONTENT, "댓글 삭제에 성공했습니다.");


    private final HttpStatus status;
    private final String message;
}
