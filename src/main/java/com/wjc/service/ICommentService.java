package com.wjc.service;

import com.github.pagehelper.PageInfo;
import com.wjc.model.domain.Comment;

public interface ICommentService {
    // 获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid, int page, int count);

    // 用户发表评论
    public void pushComment(Comment comment);

    // 删除评论
    public void deleteCommentWithId(Integer id);

    PageInfo<Comment> getAllComments(Integer aid, String startTime,
                                     String endTime,Integer pageSize,Integer pageNum);
}