package cn.edu.sdu.wh.lqy.lingxi.blog.controller.web;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.RestPageConst;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.Types;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ArticleVoSearch;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.search.ServiceMultiResult;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ISearchService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索
 */
@Controller
@RequestMapping("/lingxi/")
public class SearchController extends BaseController {


    @Autowired
    private ISearchService searchService;
    @Autowired
    private IArticleService articleService;


    /**
     * 搜索页
     *
     * @param keyword
     * @return
     */
    @GetMapping(value = "search/{keyword}")
    public String search(Model model, @PathVariable String keyword, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return search(model, keyword, 0, limit);
    }

    @GetMapping(value = "search/{keyword}/{page}")
    public String search(Model model, @PathVariable String keyword, @PathVariable int page,
                         @RequestParam(value = "limit", defaultValue = "12") int limit) {

        ArticleVoSearch articleVoSearch = new ArticleVoSearch();
        articleVoSearch.setTitle(keyword);
        articleVoSearch.setStart(page);
        articleVoSearch.setSize(limit);
        articleVoSearch.setType(Types.ARTICLE.getType());
        articleVoSearch.setStatus(Types.PUBLISH.getType());
        articleVoSearch.setOrderBy("created");
        articleVoSearch.setOrderDirection("desc");

        PageInfo<Article> articles = new PageInfo<>();

        ServiceMultiResult<Integer> serviceResult = searchService.query(articleVoSearch);

        if (serviceResult != null && serviceResult.getTotal() > 0) {
            List<Article> articleList = serviceResult.getResult().stream()
                    .map(contId -> articleService.getContents(contId + ""))
                    .collect(Collectors.toList());

            articles = new PageInfo<>(articleList);
        }
        model.addAttribute("articles", articles);
        model.addAttribute("type", "搜索");
        model.addAttribute("keyword", keyword);

        return RestPageConst.PAGE_CAT;
    }


}
