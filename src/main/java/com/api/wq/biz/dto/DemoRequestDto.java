package com.api.wq.biz.dto;

import com.api.wq.domain.IRequest;

import lombok.Data;

@Data
public class DemoRequestDto implements IRequest {
    private String orgName;

}
