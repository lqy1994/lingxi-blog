package cn.edu.sdu.wh.lqy.lingxi.blog.map;

import cn.edu.sdu.wh.lqy.lingxi.blog.BaseTests;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ArticleMapperTests extends BaseTests {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testInsert() {

        Article article = new Article();
        article.setContent("Text content heahahahah");
        article.setThumbnail("test1111");
        article.setType("1111");
        article.setStatus("status1");
        article.setAuthorId(1);
        article.setTitle("Titiel1");
        article.setCategories("default");
//        article.setId(10);

        int res = articleMapper.insertNewArticle(article);
        System.out.println(res);
        System.out.println(article.getId());
    }


    @Test
    public void testKeyHolder() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        long value = keyHolder.getKey().longValue();
        System.out.println(value);
    }
}
