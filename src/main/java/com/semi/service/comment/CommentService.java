// src/main/java/com/semi/service/comment/CommentService.java
package com.semi.service.comment;

import java.util.List;
import java.util.Map;

import com.semi.domain.Comment;

/**
 * 댓글 서비스 인터페이스
 */
public interface CommentService {

    long addComment(Comment comment) throws Exception;

    int updateComment(long commentId, String content) throws Exception;

    int deleteComment(long commentId) throws Exception;
    
    /**
     * volunteerId 기준 댓글 목록/카운트/페이지 정보를 한 번에 제공
     * 반환 Map 키: list, count, page, pageSize, totalPage
     */
    Map<String, Object> getComments(long volunteerId, int page, int pageSize) throws Exception;

    /**
     * volunteerId 기준 댓글 목록만 간단 조회
     */
    List<Comment> getCommentList(long volunteerId, int page, int pageSize) throws Exception;

    /**
     * volunteerId 기준 댓글 총 건수
     */
    int getCommentCount(long volunteerId) throws Exception;
}
