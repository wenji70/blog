package com.wjc.web.admin;

import com.wjc.model.ResponseData.ArticleResponseData;
import com.wjc.model.domain.Categories;
import com.wjc.service.IArticleService;
import com.wjc.service.ICategoriesService;
import com.wjc.service.ISiteService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Api(tags = "admin分类标签接口")
@RequestMapping("/admin")
@Controller
public class AdminCategoriesController {
    @Autowired
    private ICategoriesService iCategoriesService;
    @Autowired
    IArticleService iArticleService;
    @Autowired
    ISiteService iSiteService;

    // 跳转到分类、标签管理页面
    @GetMapping("/categories")
    public String categories(HttpServletRequest request) {
        List<Categories> categories = iCategoriesService.getAllCategories();
        request.setAttribute("categories", categories);
        return "back/categories_list";
    }

    // 添加分类
    @Operation(summary = "添加分类")
    @PostMapping("/addCategory")
    @ResponseBody
    public ArticleResponseData addCategory(@RequestParam String categoryName) {
        try {
            iCategoriesService.addCategories(categoryName);
            log.info("分类添加成功: {}", categoryName);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("分类添加失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    // 更新分类
    @Operation(summary = "更新分类")
    @PostMapping("/updateCategory")
    @ResponseBody
    public ArticleResponseData updateCategory(@RequestParam Integer id, @RequestParam String categoryName) {
        try {
            Categories category = new Categories();
            category.setId(id);
            category.setCategoriesName(categoryName);
            iCategoriesService.updateCategories(category);
            log.info("分类更新成功，ID: {}, 名称: {}", id, categoryName);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("分类更新失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    // 删除分类
    @Operation(summary = "删除分类")
    @PostMapping("/deleteCategories")
    @ResponseBody
    public ArticleResponseData deleteCategories(@RequestParam Integer id) {
        try {
            iCategoriesService.deleteCategoriesWithId(id);
            log.info("分类删除成功，ID: {}", id);
            return ArticleResponseData.ok();
        } catch (Exception e) {
            log.error("分类删除失败，错误信息: {}", e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
