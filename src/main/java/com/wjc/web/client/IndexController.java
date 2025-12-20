package com.wjc.web.client;

import com.github.pagehelper.PageInfo;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Categories;
import com.wjc.model.domain.Comment;
import com.wjc.service.IArticleService;
import com.wjc.service.ICategoriesService;
import com.wjc.service.ICommentService;
import com.wjc.service.ISiteService;
import com.wjc.service.impl.ArticleServiceImpl;
import com.wjc.service.impl.CommentServiceImpl;
import com.wjc.utils.Commons;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
//@Controller
@Controller
public class IndexController {
    @Autowired
    private IArticleService iArticleService;
    @Autowired
    private CommentServiceImpl commentServiceImpl;
    @Autowired
    private Commons commons;
    @Autowired
    private ISiteService iSiteService;
    @Autowired
    ICommentService commentService;
    @Autowired
    private ICategoriesService iCategoriesService;

    //    跳转首页
    @GetMapping(value = "/")
    private String index(HttpServletRequest request) {
        return this.index(request, 1, 5);
    }

    // 文章页
    @GetMapping(value = "/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page,
                        @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<Article> articles = iArticleService.selectArticleWithPage(page,
                count);
        request.setAttribute("articles", articles);
        request.setAttribute("commons", commons);
        log.info("分页获取文章信息: 页码 " + page + ",条数 " + count);
//        return articles.toString();
        // 获取文章热度统计信息
        List<Article> articleList = iArticleService.getHeatArticles();
        request.setAttribute("articleList", articleList);
        // 获取所有分类，用于页面显示分类名称
        List<Categories> categoriesList = iCategoriesService.getAllCategories();
        request.setAttribute("categoriesList", categoriesList);
        return "client/index";
    }

    //根据id查询文章
    @Operation(summary = "根据id查询文章")
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id,
                                 HttpServletRequest request) {
        Article article = iArticleService.selectArticleWithId(id);
        if(article!=null){
            // 查询封装评论相关数据
            getArticleComments(request, article);
            // 更新文章点击量
            iSiteService.updateStatistics(article);
            request.setAttribute("article",article);
            return "client/articleDetails";
        }else {
            log.warn("查询文章详情结果为空，查询文章id: "+id);
            // 未找到对应文章页面，跳转到提示页
            return "comm/error_404";
        }
    }

    private void getArticleComments(HttpServletRequest request, Article article) {
        if (article.getAllowComment()) {
            // cp表示评论页码，commentPage
            String cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" : cp;
            PageInfo<Comment> comments =
                    commentServiceImpl.getComments(article.getId(),Integer.parseInt(cp),3);
            request.setAttribute("cp", cp);
            request.setAttribute("comments", comments);
        }
    }
    //分页查询所有评论根据文章id，开始时间，结束时间，pageSize（默认10），pageNum
    @Operation(summary = "分页查询所有评论")
    @GetMapping("/comments/getAll")
    public String getAllComments(HttpServletRequest request,
                                 @RequestParam(required = false) Integer aid,
                                 @RequestParam(required = false) String startTime,
                                 @RequestParam(required = false) String endTime,
                                 @RequestParam(defaultValue = "10") Integer pageSize,
                                 @RequestParam(defaultValue = "1") Integer pageNum) {
        PageInfo<Comment> comments = commentService.getAllComments(aid, startTime, endTime, pageSize, pageNum);
        // 3. 将评论数据存入请求域（核心！页面通过 "comments" 这个key获取数据）
        request.setAttribute("comments", comments);
        return "comm/getAllcomments";
    }


}


