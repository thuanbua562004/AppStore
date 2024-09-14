package com.example.appstore.Interface;

public interface ApiCallback {
    void onSuccess(String response);
    void onError(String errorMessage);
}
