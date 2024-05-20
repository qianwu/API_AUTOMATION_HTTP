package com.api.wq.domain;

public interface IResponseParser {
    <T> T parseResponse(String response);
}
