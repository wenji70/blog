package com.wjc.web.client;

import com.github.pagehelper.PageInfo;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Comment;
import com.wjc.service.IArticleService;
import com.wjc.service.ISiteService;
import com.wjc.service.impl.ArticleServiceImpl;
import com.wjc.service.impl.CommentServiceImpl;
import com.wjc.utils.Commons;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        return "client/index";
    }

    //根据id查询文章
    @Operation(summary = "根据id查询文章")
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id, HttpServletRequest
            request) {
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


}


