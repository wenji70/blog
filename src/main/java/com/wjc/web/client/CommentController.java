package com.wjc.web.client;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wjc.model.ResponseData.ArticleResponseData;
import com.wjc.model.domain.Comment;
import com.wjc.model.domain.User;
import com.wjc.service.ICommentService;

import com.wjc.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = "评论接口")
@RestController
public class CommentController {
    private static final Logger logger =
            LoggerFactory.getLogger(CommentController.class);
    @Autowired
    private ICommentService commentServcie;

    // 发表评论操作
    @PostMapping(value = "/comments/publish")
    @ResponseBody
    public ArticleResponseData publishComment(HttpServletRequest request, @RequestParam
    Integer aid, @RequestParam String text) {
// 去除js脚本
        text = MyUtils.cleanXSS(text);
        text = EmojiParser.parseToAliases(text);
// 获取当前登录用户
        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = securityUser.getUsername();
// 封装评论信息
        Comment comments = new Comment();
        comments.setArticleId(aid);
        comments.setIp(request.getRemoteAddr());
        comments.setCreated(new Date());
        comments.setAuthor(username);
        comments.setContent(text);
        try {
            commentServcie.pushComment(comments);
            logger.info("发布评论成功，对应文章id: {}", aid);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("发布评论失败，对应文章id: {};错误描述: {}", aid, e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //查询评论
    @Operation(summary = "分页查询评论")
    @PostMapping("/comments/getByid")
    public String getCommentById(HttpServletRequest request, @RequestParam Integer aid, @RequestParam Integer page, @RequestParam Integer count) {
        PageInfo<Comment> comments = commentServcie.getComments(aid, page, count);
        return comments.toString();
    }

    //删除评论
    @Operation(summary = "删除评论")
    @PostMapping("/comments/delete")
    public String deleteCommentById(HttpServletRequest request, @RequestParam Integer id) {
        commentServcie.deleteCommentWithId(id);
        return "删除成功";
    }
}