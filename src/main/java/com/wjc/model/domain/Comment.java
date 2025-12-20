package com.wjc.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private Integer id;         // 评论id
    private Integer articleId;  // 评论的文章id
    private String content;     // 评论内容
    private Date created;       // 评论日期
    private String author;      // 评论作者名
    private String ip;          // 评论用户登录ip
    private String status;      // 评论状态, 默认审核通过approved
}