package com.semi.domain;

import java.util.Date;

/**
 * 댓글 도메인 객체
 * - 어느 게시글의 댓글인지 식별을 위해 volunteerId 포함
 * - 화면 표시 편의를 위해 authorName 포함(조인 결과)
 */
public class Comment {

    // ====== 식별/연결 ======
    private long commentId;   // 댓글 PK (comments.commentid)
    private long volunteerId; // 부모 게시글 ID (comments.volunteerid)

    // ====== 작성자/내용 ======
    private String authorId;    // 작성자 ID (comments.authorid)
    private String authorName;  // 작성자 이름(조인)
    private String content;     // 내용

    // ====== 시간 ======
    private Date createdAt;     // 작성시각

    // ====== getter / setter ======
    public long getCommentId() { return commentId; }
    public void setCommentId(long commentId) { this.commentId = commentId; }

    public long getVolunteerId() { return volunteerId; }
    public void setVolunteerId(long volunteerId) { this.volunteerId = volunteerId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
