// src/main/java/com/semi/service/voloffer/dto/VolOfferListItem.java
package com.semi.service.volOffer.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 봉사요청 목록 화면 전용 DTO
 * - 요구사항: 제목, 작성자이름, 봉사요청날짜(starttime), 처리상태코드
 * - 네비게이션을 위해 volunteerId도 함께 보유
 */
public class VolOfferListItem {

    // 식별자(상세/수정 이동에 사용)
    private long volunteerId;

    // 목록 표시용 필드
    private String title;
    private String authorName;
    private LocalDate date;       // starttime 의 날짜부
    private LocalTime startTime;  // starttime 의 시간부
    private String status;
    private String category;

    // Getter/Setter
    public long getVolunteerId() { return volunteerId; }
    public void setVolunteerId(long volunteerId) { this.volunteerId = volunteerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
	public String getCategory() {	return category;	}
	public void setCategory(String category) {	this.category = category;	}
}
