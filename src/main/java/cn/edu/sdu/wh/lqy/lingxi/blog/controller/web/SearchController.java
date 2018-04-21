package cn.edu.sdu.wh.lqy.lingxi.blog.controller.web;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.RestPageConst;
import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 搜索
 */
@Controller
public class SearchController extends BaseController {


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
        return search(model, keyword, 1, limit);
    }

    @GetMapping(value = "search/{keyword}/{page}")
    public String search(Model model, @PathVariable String keyword, @PathVariable int page,
                         @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > WebConstant.MAX_PAGE ? 1 : page;
        PageInfo<Article> articles = articleService.getArticles(keyword, page, limit);
        model.addAttribute("articles", articles);
        model.addAttribute("type", "搜索");
        model.addAttribute("keyword", keyword);
        return RestPageConst.PAGE_CAT;
    }


}
