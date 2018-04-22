package cn.edu.sdu.wh.lqy.lingxi.blog.map;

import cn.edu.sdu.wh.lqy.lingxi.blog.BaseTests;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ArticleMapperTests extends BaseTests {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    public void testInsert() {

        Article article = new Article();
        article.setContent("Text content heahahahah");
        article.setThumbnail("test1");
        article.setType("1111");
        article.setStatus("status1");
        article.setAuthorId(1);
        article.setTitle("Titiel1");
        article.setCategories("default");
//        article.setId(10);

        int res = articleMapper.insertNewArticle(article);
        System.out.println(res);
    }
}
