package com.semi.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 공지 엔티티
 * - 단순 공지사항(게시글과 분리)
 */
public class Notice implements Serializable {

    /** 공지 식별자 */
    private Long noticeId;
    /** 작성자 식별(사용자 userId) */
    private String authorId;
    /** 제목/내용 */
    private String title;
    private String content;
    /** 생성/수정 시각 */
    private LocalDateTime createdAt;

    public Notice() {}

    // Getter/Setter
    public Long getNoticeId() { return noticeId; }
    public void setNoticeId(Long noticeId) { this.noticeId = noticeId; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /** EL에서 사용 가능한 날짜 문자열 반환 (yyyy-MM-dd) */
    public String getCreatedAtString() {
        if (createdAt == null) return "";
        return createdAt.toLocalDate().toString();
    }

	@Override
	public String toString() {
		return "Notice [noticeId=" + noticeId + ", authorId=" + authorId + ", title=" + title + ", content=" + content
				+ ", createdAt=" + createdAt + "]";
	}

}
