package com.wjc.dao;

import com.wjc.model.domain.Categories;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface CategoriesMapper {

    // 查询所有分类
    @Select("SELECT * FROM t_categories")
    public List<Categories> getAllCategories();

    // 根据id查询分类
    @Select("SELECT * FROM t_categories WHERE id=#{id}")
    public Categories getCategoriesWithId(Integer id);

    // 根据文章id查询分类
    @Select("SELECT * FROM t_categories WHERE id IN (SELECT category_id FROM t_article WHERE id=#{aid})")
    public Categories getCategoriesWithArticleId(Integer aid);

    // 添加分类
    @Insert("INSERT INTO t_categories (categoriesName) VALUES (#{categoriesName})")
    public void addCategories(Categories categories);

    // 更新分类
    @Update("UPDATE t_categories SET categoriesName=#{categoriesName} WHERE id=#{id}")
    public void updateCategories(Categories category);

    // 删除分类
    @Delete("DELETE FROM t_categories WHERE id=#{id}")
    public void deleteCategoriesWithId(Integer id);
}
