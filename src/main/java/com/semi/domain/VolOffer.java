package com.semi.domain;

import java.io.Serializable;

/**
 * 봉사등록(Offer) 게시글
 * - Post 공통 속성 + 오퍼 전용 플래그
 */
public class VolOffer extends Post implements Serializable {

    /** 오퍼 전용 플래그(다이어그램의 offerFlag) */
    private String offerFlag;

    public String getOfferFlag() { return offerFlag; }
    public void setOfferFlag(String offerFlag) { this.offerFlag = offerFlag; }
    
    @Override
    public String toString() {
        return "VolOffer{" +
                "offerFlag=" + offerFlag +
                ", " + super.toString() +  // Post.toString() 결과 붙이기
                '}';
    }
}
