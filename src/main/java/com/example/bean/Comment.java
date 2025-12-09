package com.example.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Comment {
    private Integer id;
    private Integer articleId;
    private Date created;
    private String ip;
    private String content;
    private String status;
    private String author;
}
