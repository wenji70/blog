package com.wjc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wjc.dao.ArticleMapper;
import com.wjc.dao.CommentMapper;
import com.wjc.dao.StatisticMapper;
import com.wjc.model.domain.Comment;
import com.wjc.model.domain.Statistic;
import com.wjc.service.IArticleService;
import com.wjc.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    // 根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page, count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }

    // 用户发表评论
    @Override
    public void pushComment(Comment comment) {
        //判断该文章是否允许评论
//        Integer a = articleMapper.getAllow(comment.getArticleId());
        // 发表一条评论，保存到数据库中
        commentMapper.pushComment(comment);
        // 更新文章评论数据，修改统计表中的数据
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum() + 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

    @Override
    public void deleteCommentWithId(Integer aid) {
        // 删除一条评论
        commentMapper.deleteCommentWithId(aid);
        // 更新文章评论数据，修改统计表中的数据
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(aid);
        statistic.setCommentsNum(statistic.getCommentsNum() - 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

    @Override
    public void updateComment(Comment newcomment) {

        newcomment.setCreated(new Date());
        commentMapper.updateComment(newcomment);
    }

}