package com.example.mars;

public interface ApiRequestResponse {
    void success(String response);

    void failure(String response);

}