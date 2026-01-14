package com.semi.domain;

import java.io.Serializable;

/**
 * 봉사요청(Request) 게시글
 * - Post 공통 속성 + 요청 전용 플래그
 */
public class VolRequest extends Post implements Serializable {

    /** 요청 전용 플래그(다이어그램의 requestFlag) */
    private String requestFlag;

    public String getRequestFlag() { return requestFlag; }
    public void setRequestFlag(String requestFlag) { this.requestFlag = requestFlag; }

    @Override
    public String toString() {
        return "VolRequest{" +
                "requestFlag=" + requestFlag +
                ", " + super.toString() +  // Post.toString() 결과 붙이기
                '}';
    }
}
