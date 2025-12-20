package com.wjc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wjc.dao.ArticleMapper;
import com.wjc.dao.CategoriesMapper;
import com.wjc.dao.CommentMapper;
import com.wjc.dao.StatisticMapper;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Categories;
import com.wjc.model.domain.Statistic;
import com.wjc.service.IArticleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CategoriesMapper categoriesMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    // 根据id查询单个文章详情
    @Override
    public Article selectArticleWithId(Integer id) {
        Article article = null;
        // 获取key
        try {
            Object o = redisTemplate.opsForValue().get("article_" + id);
            if (o != null) {
                article = (Article) o;
            }
        } catch (Exception e) {
            // 反序列化失败，删除旧缓存
            redisTemplate.delete("article_" + id);
        }
        
        // 如果缓存中没有或反序列化失败，从数据库查询
        if (article == null) {
            article = articleMapper.selectArticleWithId(id);
            if (article != null) {
                redisTemplate.opsForValue().set("article_" + id, article);
            }
        }
        return article;
    }

    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        // 封装文章统计数据
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            // 查询文章的点击量和评论量
            Statistic statistic =
                    statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return pageInfo;
    }

    @Operation(summary = "发布文章")
    @Override
    public void publish(Article article) {
        // 去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        // 设置文章创建时间
        article.setCreated(new Date());
        // 插入文章
        articleMapper.publishArticle(article);
        // 针对新文章，同时插入文章统计数据，对统计数据进行初始化
        Statistic statistic = new Statistic();
        statistic.setArticleId(article.getId());
        statistic.setHits(0);
        statistic.setCommentsNum(0);
        statisticMapper.addStatistic(statistic);
    }

    @Operation(summary = "更新文章")
    // 更新文章
    @Override
    public void updateArticleWithId(Article article) {
        // 设置修改时间
        article.setModified(new Date());
        article.setAllowComment(article.getAllowComment());
        articleMapper.updateArticleWithId(article);
        redisTemplate.delete("article_" + article.getId());
    }

    @Operation(summary = "删除文章")
    // 删除文章
    @Override
    public void deleteArticleWithId(int id) {
        // 删除文章的同时，删除对应的缓存
        articleMapper.deleteArticleWithId(id);
        redisTemplate.delete("article_" + id);
        // 同时删除对应文章的统计数据
        statisticMapper.deleteStatisticWithId(id);
        // 同时删除对应文章的评论数据
        commentMapper.deleteCommentWithId(id);
    }
    // 统计前10的热度文章信息
    @Override
    public List<Article> getHeatArticles() {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articlelist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Article article =
                    articleMapper.selectArticleWithId(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articlelist.add(article);
            if(i>=9){
                break;
            }
        }
        return articlelist;
    }

}