package com.example.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AdminUser {
    private Integer id;
    private String username;
    private String password;
    private Date createdAt;
}
