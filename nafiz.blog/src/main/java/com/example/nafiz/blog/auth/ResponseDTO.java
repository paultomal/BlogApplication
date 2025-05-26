package com.example.nafiz.blog.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private String token;
    private Date expiredDate;
    private String username;
    private Long userId;
    private List roles;
}
