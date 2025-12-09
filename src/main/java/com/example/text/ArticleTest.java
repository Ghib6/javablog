package com.example.text;

import com.example.bean.Article;
import com.example.dao.ArticleMapper;
import com.example.util.ArticleUtil;
import com.example.util.MyBatisUtils;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class ArticleTest {

    @Test
    public void testAddArticle() {
        ArticleUtil util = new ArticleUtil();
        Article article = new Article();
        article.setTitle("测试文章");
        article.setContent("这是测试文章的内容");
        article.setCreated(new Date());
        article.setCategories("测试分类");
        article.setTags("测试标签");
        article.setAllowComment(true);
        article.setThumbnail("https://picbed.ghib6.com/img/1.png");

        int res = util.addArticle(article);
        System.out.println("插入结果: " + res);
    }

    @Test
    public void testGetArticles() {
        ArticleUtil util = new ArticleUtil();
        List<Article> articles = util.getArticles();
        for (Article article : articles) {
            System.out.println(article);
        }
    }

    @Test
    public void testUpdateArticle() {
        ArticleUtil util = new ArticleUtil();
        int id = 52; // 直接写死要修改的文章ID
        Article article = util.getArticleById(id);
        if (article != null) {
            article.setTitle("修改后的标题");
            int res = util.updateArticle(article);
            System.out.println("更新结果: " + res);
        } else {
            System.out.println("未找到要修改的文章");
        }
    }

    @Test
    public void testDeleteArticle() {
        ArticleUtil util = new ArticleUtil();
        int id = 53; // 直接写死要删除的文章ID
        int res = util.deleteArticle(id);
        System.out.println("删除结果: " + res);

    }

    // 用注解查询
    @Test
    public void testSelectWorker() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        Article article = mapper.selectArticleById(2);
        System.out.println(article);
        session.close();
    }

    @Test
    public void testSelectAllArticles() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        List<Article> articles = mapper.selectAllArticles();
        for (Article article : articles) {
            System.out.println(article);
        }
        session.close();
    }

    @Test
    public void testSelectArticlesByKeyword() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        List<Article> articles = mapper.selectArticlesByKeyword("测试", null);
        for (Article article : articles) {
            System.out.println(article);
        }
        session.close();
    }

    @Test
    public void testInsertArticle() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        Article article = new Article();
        article.setTitle("注解方式插入的文章");
        article.setContent("这是通过注解方式插入的文章内容");
        article.setCreated(new Date());
        article.setCategories("注解分类");
        article.setTags("注解标签");
        article.setAllowComment(true);
        article.setThumbnail("https://picbed.ghib6.com/img/3.png");

        int res = mapper.insertArticle(article);
        System.out.println("插入结果: " + res);
        System.out.println("新插入文章的ID: " + article.getId());

        session.commit();
        session.close();

    }

    @Test
    public void testUpdateArticleByAnnotation() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        Article article = mapper.selectArticleById(58);
        if (article != null) {
            article.setTitle("注解方式修改后的标题");
            int res = mapper.updateArticle(article);
            System.out.println("更新结果: " + res);
            session.commit();
        } else {
            System.out.println("未找到要修改的文章");
        }
        session.close();
    }

    @Test
    public void testDeleteArticleByAnnotation() {
        SqlSession session = MyBatisUtils.getSqlSessionFactory().openSession();
        ArticleMapper mapper = session.getMapper(ArticleMapper.class);
        int res = mapper.deleteArticle(58); // 直接写死要删除的文章ID
        System.out.println("删除结果: " + res);
        session.commit();
        session.close();
    }

}
