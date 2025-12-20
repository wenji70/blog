package com.wjc.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Article {
    private Integer id;          // 文章id
    private String title;        // 文章标题
    private String content;      // 文章内容
    private Date created;          // 文章创建时间
    private Date modified;       // 文章修改时间
    private Integer categories;   // 文章分类
    private String tags;         // 文章标签
    private Boolean allowComment; // 是否允许评论,默认为true
    private String thumbnail;    // 文章缩略图
    private Integer hits;// 点击量
    private Integer commentsNum;  // 评论总量

}
