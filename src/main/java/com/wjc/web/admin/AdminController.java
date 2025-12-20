package com.wjc.web.admin;

import com.github.pagehelper.PageInfo;
import com.wjc.model.ResponseData.ArticleResponseData;
import com.wjc.model.domain.Article;
import com.wjc.service.IArticleService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "admin接口")
@RestController
public class AdminController {
    @Autowired
    IArticleService iArticleService;
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

    //根据id查询文章
   /* @Operation(summary = "根据id查询文章")
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id") Integer id, HttpServletRequest
            request){
        Article article = iArticleService.selectArticleWithId(id);
        return  article.toString();
    }*/


}
