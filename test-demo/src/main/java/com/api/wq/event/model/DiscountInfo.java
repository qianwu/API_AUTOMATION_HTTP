package com.api.wq.event.model;

import lombok.*;

@Data
public class DiscountInfo {
    private long discountId;
    private String texts;
    private boolean sendToPipAndPlp;
    private boolean active;
    private String status;
    private boolean couponDiscount;
    private String coupons;
    private String discountLevel;
    private String discountType;
    private String channels;
    private String stores;
    private String customer;
    private String qualificationGroup;
    private String combinedQualification;
    private String reward;
    private String redemptionLimits;
    private String startDate;
    private String endDate;
}
