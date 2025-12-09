package com.example.util;

import com.example.bean.Article;
import com.example.bean.Comment;
import com.example.dao.ArticleMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArticleUtil {

    private static final MarkdownService MARKDOWN_SERVICE = new MarkdownService();

    public int addArticle(Article article) {
        renderMarkdownContent(article);
        ensureDefaultsForCreate(article);
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            return mapper.insertArticle(article);
        } catch (Exception e) {
            throw new RuntimeException("插入文章失败", e);
        }
    }

    public List<Article> getArticles() {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            List<Article> articles = mapper.selectAllArticles();
            return articles != null ? articles : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("查询文章列表失败", e);
        }
    }

    public Article getArticleById(int id) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            return mapper.selectArticleById(id);
        } catch (Exception e) {
            throw new RuntimeException("按ID查询文章失败", e);
        }
    }

    public List<Article> searchArticles(String keyword) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            String query = keyword == null ? "" : keyword;
            List<Article> articles = mapper.selectArticlesByKeyword(query, "");
            return articles != null ? articles : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("查询文章失败", e);
        }
    }

    public List<Article> searchArticles(String keyword, String category) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            String query = keyword == null ? "" : keyword;
            String cat = category == null ? "" : category.trim();
            List<Article> articles = mapper.selectArticlesByKeyword(query, cat);
            return articles != null ? articles : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("按分类查询文章失败", e);
        }
    }

    public List<Comment> getCommentsByArticleId(int articleId) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            List<Comment> comments = mapper.selectCommentsByArticleId(articleId);
            return comments != null ? comments : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("查询评论失败", e);
        }
    }

    public int updateArticle(Article article) {
        renderMarkdownContent(article);
        ensureDefaultsForUpdate(article);
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            return mapper.updateArticle(article);
        } catch (Exception e) {
            throw new RuntimeException("更新文章失败", e);
        }
    }

    public int deleteArticle(int id) {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            return mapper.deleteArticle(id);
        } catch (Exception e) {
            throw new RuntimeException("删除文章失败", e);
        }
    }

    public List<String> getDistinctCategories() {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            List<String> cats = mapper.selectDistinctCategories();
            return cats != null ? cats : Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException("查询分类失败", e);
        }
    }

    public Map<String, Integer> getCategoryCounts() {
        try (SqlSession session = MyBatisUtils.openSession()) {
            ArticleMapper mapper = session.getMapper(ArticleMapper.class);
            List<String> fields = mapper.selectAllCategoriesFields();
            Map<String, Integer> counts = new LinkedHashMap<>();
            if (fields != null) {
                for (String field : fields) {
                    if (field == null || field.trim().isEmpty())
                        continue;
                    String[] parts = field.split(",");
                    for (String p : parts) {
                        String cat = p.trim();
                        if (cat.isEmpty())
                            continue;
                        counts.put(cat, counts.getOrDefault(cat, 0) + 1);
                    }
                }
            }
            return counts;
        } catch (Exception e) {
            throw new RuntimeException("查询分类统计失败", e);
        }
    }

    public String buildMetaDescription(Article article) {
        if (article == null) {
            return "";
        }
        return MARKDOWN_SERVICE.extractDescription(article.getContent(), 160);
    }

    private void ensureDefaultsForCreate(Article article) {
        if (article.getCreated() == null) {
            article.setCreated(new Date());
        }
        if (article.getModified() == null) {
            article.setModified(article.getCreated());
        }
        if (article.getAllowComment() == null) {
            article.setAllowComment(Boolean.TRUE);
        }
    }

    private void ensureDefaultsForUpdate(Article article) {
        if (article.getModified() == null) {
            article.setModified(new Date());
        }
        if (article.getAllowComment() == null) {
            article.setAllowComment(Boolean.TRUE);
        }
    }

    private void renderMarkdownContent(Article article) {
        if (article == null) {
            return;
        }
        String markdown = article.getMarkdown();
        if (markdown == null || markdown.trim().isEmpty()) {
            markdown = article.getContent();
        }
        String safeHtml = MARKDOWN_SERVICE.toSafeHtml(markdown == null ? "" : markdown);
        article.setMarkdown(markdown);
        article.setContent(safeHtml);
    }
}
