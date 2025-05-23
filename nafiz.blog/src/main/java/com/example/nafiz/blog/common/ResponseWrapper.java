package com.example.nafiz.blog.common;


import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper {

    public static Map<String, Object> wrap(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return response;
    }
}