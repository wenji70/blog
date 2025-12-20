package com.wjc.service;

import com.wjc.model.ResponseData.StaticticsBo;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Comment;

import java.util.List;

public interface ISiteService {
    public void updateStatistics(Article article);
    // 最新收到的评论
    public List<Comment> recentComments(int count);
    // 最新发表的文章
    public List<Article> recentArticles(int count);
    // 获取后台统计数据
    public StaticticsBo getStatistics();
}
