package cn.edu.sdu.wh.lqy.lingxi.blog.es;

import cn.edu.sdu.wh.lqy.lingxi.blog.BaseTests;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ArticleSearch;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.search.ServiceMultiResult;
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
//            article.setWordCnt(article.getContent().length());
//            articleMapper.updateById(article);
            searchService.index(article.getId());
        }
    }

    @Test
    public void testQuery() {
        ArticleSearch voSearch = new ArticleSearch();
        voSearch.setStart(0);
        voSearch.setSize(5);
        voSearch.setTitle("Redis");
        ServiceMultiResult<Integer> query = searchService.query(voSearch);
        System.out.println(gson.toJson(query.getResult()));
    }
}
