package com.example.nafiz.blog.user;

import lombok.Data;

@Data
public class UserInfoDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String contact;
    private String roles;
}
