package com.wjc.service.impl;

import com.github.pagehelper.PageHelper;
import com.wjc.dao.ArticleMapper;
import com.wjc.dao.CommentMapper;
import com.wjc.dao.StatisticMapper;
import com.wjc.model.ResponseData.StaticticsBo;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Comment;
import com.wjc.model.domain.Statistic;
import com.wjc.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements ISiteService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic =
                statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits() + 1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }
    @Override
// 最新收到的评论
    public List<Comment> recentComments(int limit) {
        PageHelper.startPage(1, limit>10 || limit<1 ? 10:limit);
        List<Comment> byPage = commentMapper.selectNewComment();
        return byPage;
    }
    @Override
// 最新发表的文章
    public List<Article> recentArticles(int limit) {
        PageHelper.startPage(1, limit>10 || limit<1 ? 10:limit);
        List<Article> list = articleMapper.selectArticleWithPage();
// 封装文章统计数据
        for (int i = 0; i < list.size(); i++) {
            Article article = list.get(i);
            Statistic statistic =
                    statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }
    @Override
// 获取后台统计数据
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }
}
