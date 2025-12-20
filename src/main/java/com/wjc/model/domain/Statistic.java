package com.wjc.model.domain;

import lombok.Data;

@Data
public class Statistic {
    private Integer id;          // 主键id
    private Integer articleId;   // 关联的文章id
    private Integer hits;        // 文章的点击量
    private Integer commentsNum; // 文章的评论总量
}