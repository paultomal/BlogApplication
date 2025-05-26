package com.example.nafiz.blog.user;

import com.example.nafiz.blog.blog.BlogEntity;
import com.example.nafiz.blog.common.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Column(unique = true)
    @NotEmpty(message = "UserName should not be empty")
    private String username;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    @NotEmpty(message = "Email should not be empty")
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles roles;

    private String contact;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userInfo")
    private List<BlogEntity> blogs;
}
