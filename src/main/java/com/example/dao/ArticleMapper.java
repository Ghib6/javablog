package com.example.dao;

import com.example.bean.Article;
import com.example.bean.Comment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ArticleMapper {

        @Select("SELECT * FROM t_article WHERE id = #{id}")
        Article selectArticleById(@Param("id") int id);

        @Select("SELECT * FROM t_article ORDER BY created DESC")
        List<Article> selectAllArticles();

        @Select("SELECT * FROM t_article WHERE (#{keyword} IS NULL OR #{keyword} = '' "
                        + "OR title LIKE CONCAT('%', #{keyword}, '%') "
                        + "OR tags LIKE CONCAT('%', #{keyword}, '%')) "
                        + "AND (#{category} IS NULL OR #{category} = '' OR FIND_IN_SET(#{category}, categories) > 0) "
                        + "ORDER BY created DESC")
        List<Article> selectArticlesByKeyword(@Param("keyword") String keyword,
                        @Param("category") String category);

        @Results(id = "commentResultMap", value = {
                        @Result(property = "id", column = "id"),
                        @Result(property = "articleId", column = "article_id"),
                        @Result(property = "created", column = "created"),
                        @Result(property = "ip", column = "ip"),
                        @Result(property = "content", column = "content"),
                        @Result(property = "status", column = "status"),
                        @Result(property = "author", column = "author")
        })
        @Select("SELECT id, article_id, created, ip, content, status, author " +
                        "FROM t_comment WHERE article_id = #{articleId} ORDER BY created DESC")
        List<Comment> selectCommentsByArticleId(@Param("articleId") int articleId);

        @Insert("INSERT INTO t_article (title, content, markdown, created, categories, tags, allow_comment, thumbnail) "
                        +
                        "VALUES (#{title}, #{content}, #{markdown}, #{created}, #{categories}, #{tags}, #{allowComment}, #{thumbnail})")
        @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
        int insertArticle(Article article);

        @Update("UPDATE t_article SET title = #{title}, content = #{content}, markdown = #{markdown}, categories = #{categories}, tags = #{tags}, "
                        +
                        "allow_comment = #{allowComment}, thumbnail = #{thumbnail}, modified = #{modified} WHERE id = #{id}")
        int updateArticle(Article article);

        @Delete("DELETE FROM t_article WHERE id = #{id}")
        int deleteArticle(@Param("id") int id);

        @Select("SELECT DISTINCT categories FROM t_article WHERE categories IS NOT NULL AND categories <> ''")
        List<String> selectDistinctCategories();

        @Select("SELECT categories FROM t_article WHERE categories IS NOT NULL AND categories <> ''")
        List<String> selectAllCategoriesFields();

}
