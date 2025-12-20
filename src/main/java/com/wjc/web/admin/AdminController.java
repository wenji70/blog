package com.wjc.web.admin;

import com.github.pagehelper.PageInfo;
import com.wjc.model.ResponseData.ArticleResponseData;
import com.wjc.model.ResponseData.StaticticsBo;
import com.wjc.model.domain.Article;
import com.wjc.model.domain.Categories;
import com.wjc.model.domain.Comment;
import com.wjc.service.IArticleService;
import com.wjc.service.ICategoriesService;
import com.wjc.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "admin接口")
@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    IArticleService iArticleService;
    @Autowired
    ISiteService iSiteService;
    @Autowired
    ICategoriesService iCategoriesService;

    //添加文章
    @Operation(summary = "添加文章")
    @PostMapping("/addArticle")
    public ArticleResponseData addArticle(@RequestBody Article article) {
        iArticleService.publish(article);
        ArticleResponseData a = new ArticleResponseData(true);
        a.setMsg("添加成功");
        return a;
    }

    //删除文章
    @PostMapping("/deleteArticle")
    public ArticleResponseData deleteArticle(Integer id) {
        iArticleService.deleteArticleWithId(id);
        ArticleResponseData a = new ArticleResponseData(true);
        a.setMsg("删除成功");
        return a;
    }

    //根据id更新文章
    @PostMapping("/updateArticle")
    public ArticleResponseData updateArticle(@RequestBody Article article) {
        iArticleService.updateArticleWithId(article);
        ArticleResponseData a = new ArticleResponseData(true);
        a.setMsg("更新成功");
        return a;
    }

    // 管理中心起始页
    @GetMapping(value = {"", "/index"})
    public String index(HttpServletRequest request) {
        // 获取最新的5篇博客、评论以及统计数据
        List<Article> articles = iSiteService.recentArticles(5);
        List<Comment> comments = iSiteService.recentComments(5);
        StaticticsBo staticticsBo = iSiteService.getStatistics();
        // 向Request域中存储数据
        request.setAttribute("comments", comments);
        request.setAttribute("articles", articles);
        request.setAttribute("statistics", staticticsBo);
        return "back/index";
    }

    // 向文章发表页面跳转
    @GetMapping(value = "/article/toEditPage")
    public String newArticle(HttpServletRequest request) {
        // 获取所有分类
        List<Categories> categoriesList = iCategoriesService.getAllCategories();
        request.setAttribute("categoriesList", categoriesList);
        return "back/article_edit";
    }

    // 发表文章
    @PostMapping(value = "/article/publish")
    @ResponseBody
    public ArticleResponseData publishArticle(Article article) {
        // 处理空分类，设置为第一个分类
        if (article.getCategories() == null || article.getCategories() == 0) {
            List<Categories> categoriesList = iCategoriesService.getAllCategories();
            if (categoriesList != null && !categoriesList.isEmpty()) {
                article.setCategories(categoriesList.get(0).getId());
            }
        }

        try {
            iArticleService.publish(article);
            log.info("文章发布成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("文章发布失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    // 跳转到后台文章列表页面
    @GetMapping(value = "/article")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "count", defaultValue = "10") int count,
                        HttpServletRequest request) {
        PageInfo<Article> pageInfo = iArticleService.selectArticleWithPage(page,
                count);
        request.setAttribute("articles", pageInfo);
        // 获取所有分类，用于页面显示分类名称
        List<Categories> categoriesList = iCategoriesService.getAllCategories();
        request.setAttribute("categoriesList", categoriesList);
        return "back/article_list";
    }

    // 向文章修改页面跳转
    @GetMapping(value = "/article/{id}")
    public String editArticle(@PathVariable("id") String id, HttpServletRequest
            request) {
        Article article = iArticleService.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents", article);
        request.setAttribute("categories", article.getCategories());
        // 获取所有分类
        List<Categories> categoriesList = iCategoriesService.getAllCategories();
        request.setAttribute("categoriesList", categoriesList);
        return "back/article_edit";

    }

    // 文章修改处理
    @PostMapping(value = "/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article) {
        try {
            // 处理空分类，设置为第一个分类
            if (article.getCategories() == null || article.getCategories() == 0) {
                List<Categories> categoriesList = iCategoriesService.getAllCategories();
                if (categoriesList != null && !categoriesList.isEmpty()) {
                    article.setCategories(categoriesList.get(0).getId());
                }
            }
            iArticleService.updateArticleWithId(article);
            log.info("文章更新成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("文章更新失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    // 文章删除
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            iArticleService.deleteArticleWithId(id);
            log.info("文章删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("文章删除失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }


    //根据id查询文章
   /* @Operation(summary = "根据id查询文章")
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id, HttpServletRequest
            request){
        Article article = iArticleService.selectArticleWithId(id);
        return  article.toString();
    }*/


}
