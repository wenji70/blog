package com.wjc.service;

import com.wjc.model.domain.Categories;

import java.util.List;

public interface ICategoriesService {
    // 查询所有分类
    public List<Categories> getAllCategories();

    // 根据id查询分类
    public Categories getCategoriesWithId(Integer id);

    // 根据文章id查询分类
    public Categories getCategoriesWithArticleId(Integer aid);

    // 添加分类
    public void addCategories(String categoriesName);

    // 更新分类
    public void updateCategories(Categories category);

    // 删除分类
    public void deleteCategoriesWithId(Integer id);
}
