package com.wjc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wjc.dao.ArticleMapper;
import com.wjc.dao.CommentMapper;
import com.wjc.dao.StatisticMapper;
import com.wjc.model.domain.Comment;
import com.wjc.model.domain.Statistic;
import com.wjc.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return new PageInfo<>(commentList);
    }

    // 用户发表评论
    @Override
    public void pushComment(Comment comment) {
        // 发表一条评论，保存到数据库中
        commentMapper.pushComment(comment);
        // 更新文章评论数据，修改统计表中的数据
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum() + 1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }

    @Override
    public void deleteCommentWithId(Integer id) {
        // 先查询评论，获取文章ID
        Comment comment = commentMapper.selectCommentById(id);
        if (comment == null) {
            return; // 评论不存在，直接返回
        }
        // 删除一条评论
        commentMapper.deleteCommentWithId(id);
        // 更新文章评论数据，修改统计表中的数据
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        if (statistic != null) {
            statistic.setCommentsNum(statistic.getCommentsNum() - 1);
            statisticMapper.updateArticleCommentsWithId(statistic);
        }
    }

    @Override
    public PageInfo<Comment> getAllComments(Integer aid, String startTime, String endTime,
                                            Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.getAllComments(aid, startTime,endTime);
        return new PageInfo<>(list);
    }

}