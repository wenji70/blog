package com.wjc.dao;

import com.wjc.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //根据id查询文章信息
    @Select("select * from t_article where id=#{id}")
    public Article selectArticleWithId(int id);

    //分页
    //分页查询
    @Select("SELECT * FROM t_article ORDER BY id DESC")
    public List<Article> selectArticleWithPage();

    //发表文章
    @Insert("INSERT INTO t_article (title,created,modified,tags,categories," +
            " allow_comment, thumbnail, content)" +
            " VALUES (#{title},#{created}, #{modified}, #{tags}, #{categories}," +
            " #{allowComment}, #{thumbnail}, #{content})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id") //@Options注解会自动为表对应的对象的主键字段设置上自增的值，直接从这个对象中获取即可
    public Integer publishArticle(Article article);

    //删除文章
    @Delete("DELETE FROM t_article WHERE id=#{id}")
    public void deleteArticleWithId(int id);

    //根据id更新文章
    public Integer updateArticleWithId(Article article);
    // 站点服务统计，统计文章数量
    @Select("SELECT COUNT(1) FROM t_article")
    public Integer countArticle();

}