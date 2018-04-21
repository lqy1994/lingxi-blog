package cn.edu.sdu.wh.lqy.lingxi.blog.es;

import cn.edu.sdu.wh.lqy.lingxi.blog.BaseTests;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.ArticleVoSearch;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.search.ServiceMultiResult;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ISearchService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ArticleIndexTests extends BaseTests {

    @Autowired
    private ISearchService searchService;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private Gson gson;


    @Test
    public void testIndex() {
        List<Article> articleList = articleMapper.selectList(new EntityWrapper<>());
        for (Article article : articleList) {
            System.out.println(article.getId() + "-------->");
            searchService.index(article.getId());
        }
    }

    @Test
    public void testQuery() {
        ArticleVoSearch voSearch = new ArticleVoSearch();
        voSearch.setStart(0);
        voSearch.setSize(5);
        voSearch.setTitle("Redis");
        ServiceMultiResult<Integer> query = searchService.query(voSearch);
        System.out.println(gson.toJson(query.getResult()));
    }
}
