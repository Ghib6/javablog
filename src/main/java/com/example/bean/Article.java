package com.example.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Article {
    private Integer id; // 文章id
    private String title; // 文章标题
    private String content; // 文章内容
    private Date created; // 文章创建时间
    private Date modified; // 文章创建时间
    private String categories; // 文章分类
    private String tags; // 文章标签
    private Boolean allowComment; // 是否允许评论，默认为true
    private String thumbnail; // 文章缩略图
    private String markdown; // 原始Markdown内容

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                // ", content='" + content + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", categories='" + categories + '\'' +
                ", tags='" + tags + '\'' +
                ", allowComment=" + allowComment +
                ", thumbnail='" + thumbnail + '\'' +
                '}' + '\n';
    }
}
