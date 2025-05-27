package com.example.nafiz.blog.common;


import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper {

    public static Map<String, Object> wrap(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    public static Map<String, Object> wrapFailure(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "failed");
        response.put("message", message);
        response.put("data", null);
        return response;
    }
}
