package com.api.wq.domain;

public class BaseResponseParser implements IResponseParser {

    @Override
    public String parseResponse(String response) {
        return response;
    }
}
