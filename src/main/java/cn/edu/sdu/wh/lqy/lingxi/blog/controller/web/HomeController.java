package cn.edu.sdu.wh.lqy.lingxi.blog.controller.web;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页
 */
@Controller
public class HomeController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    public static final String INDEX = "themes/default/index";

    @Autowired
    private IArticleService articleService;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping(value = "/")
    public String index(Model model, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return index(model, 1, limit);
    }

    /**
     * 首页分页
     *
     * @param model model
     * @param offset       第几页
     * @param limit   每页大小
     * @return 主页
     */
    @GetMapping(value = "page/{offset}")
    public String index(Model model, @PathVariable("offset") int offset, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        offset = offset < 0 || offset > WebConstant.MAX_PAGE ? 1 : offset;
        PageInfo<Article> articles = articleService.getContents(offset, limit);
        model.addAttribute("articles", articles);
        if (offset > 1) {
            model.addAttribute("title", "第" + offset + "页");
        }
        return INDEX;
    }



}
