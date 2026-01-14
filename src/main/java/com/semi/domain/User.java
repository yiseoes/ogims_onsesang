package com.semi.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 사용자 엔티티
 * - 로그인/식별에 쓰이는 userId 중심 (PK)
 */
public class User implements Serializable {

    // ====== 식별/계정 기본 ======
    /** 로그인/표시용 사용자 아이디 (PK) */
    private String userId;
    
    private String password;
    
    /** 이름 */
    private String name;

    // ====== 프로필/연락 ======
    /** 지역 */
    private String region;
    /** 전화번호 */
    private String phone;
    /** 성별 */
    private String gender;
    /** 생년월일 */
    private LocalDate birthDate;

    // ====== 분류/상태 ======
    /** 카테고리(사용자 분류가 필요하면 사용, 없으면 공란) */
    private String category;
    /** 사용자 상태(예: ACTIVE, SUSPENDED 등) */
    private String status;

    public User() {}

    // 동등성: userId 기반
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User other = (User) o;
        return Objects.equals(userId, other.userId);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(userId); 
    }

    // Getter/Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getPassword() {	return password;	}
	public void setPassword(String password) {	this.password = password;	}

	public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", name=" + name + ", region=" + region + ", phone=" + phone
				+ ", gender=" + gender + ", birthDate=" + birthDate + ", category=" + category + ", status=" + status + "]";
	}
}
