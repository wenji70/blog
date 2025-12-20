package com.wjc.service.impl;

import com.wjc.dao.CategoriesMapper;
import com.wjc.model.domain.Categories;
import com.wjc.service.ICategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements ICategoriesService {
    @Autowired
    private CategoriesMapper categoriesMapper;

    @Override
    public List<Categories> getAllCategories() {
        return categoriesMapper.getAllCategories();
    }

    @Override
    public Categories getCategoriesWithId(Integer id) {
        return categoriesMapper.getCategoriesWithId(id);
    }

    @Override
    public Categories getCategoriesWithArticleId(Integer aid) {
        return categoriesMapper.getCategoriesWithArticleId(aid);
    }

    @Override
    public void addCategories(String categoriesName) {
        Categories categories1 = new Categories();
        categories1.setCategoriesName( categoriesName);
        categoriesMapper.addCategories(categories1);
    }

    @Override
    public void updateCategories(Categories category) {
        categoriesMapper.updateCategories(category);
    }

    @Override
    public void deleteCategoriesWithId(Integer id) {
        categoriesMapper.deleteCategoriesWithId(id);
    }
}
