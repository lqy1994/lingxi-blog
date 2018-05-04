package cn.edu.sdu.wh.lqy.lingxi.blog.controller.admin;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.RestPageConst;
import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.exception.LingXiException;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Bo.ApiResponse;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ContentVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Meta;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.User;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.LogActions;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.TypeEnum;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ILogService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IMetaService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/article")
@Transactional(rollbackFor = LingXiException.class)
public class AdminArticleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminArticleController.class);

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IMetaService metasService;

    @Autowired
    private ILogService logService;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType());
        PageInfo<Article> contentsPaginator = articleService.getArticlesWithpage(contentVoExample, page, limit);
        request.setAttribute("articles", contentsPaginator);
        return RestPageConst.ADMIN_ARTICLE_LIST;
    }

    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<Meta> categories = metasService.getMetas(TypeEnum.CATEGORY.getType());
        request.setAttribute("categories", categories);
        return RestPageConst.ADMIN_ARTICLE_EDIT;
    }

    @GetMapping(value = "/{id}")
    public String editArticle(@PathVariable String id, HttpServletRequest request) {
        Article contents = articleService.getArticle(id);
        request.setAttribute("contents", contents);
        List<Meta> categories = metasService.getMetas(TypeEnum.CATEGORY.getType());
        request.setAttribute("categories", categories);
        request.setAttribute("active", "article");
        return RestPageConst.ADMIN_ARTICLE_EDIT;
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    public ApiResponse publishArticle(Article article, HttpServletRequest request) {
        User users = this.user(request);
        article.setAuthorId(users.getUid());
        article.setType(TypeEnum.ARTICLE.getType());
        if (StringUtils.isBlank(article.getCategories())) {
            article.setCategories("默认分类");
        }
        String result = articleService.publish(article);
        if (!WebConstant.SUCCESS_RESULT.equals(result)) {
            return ApiResponse.fail(result);
        }

        return ApiResponse.ok();
    }

    @PostMapping(value = "/modify")
    @ResponseBody
    public ApiResponse modifyArticle(Article contents, HttpServletRequest request) {
        User users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(TypeEnum.ARTICLE.getType());
        String result = articleService.updateArticle(contents);
        if (!WebConstant.SUCCESS_RESULT.equals(result)) {
            return ApiResponse.fail(result);
        }
        return ApiResponse.ok();
    }

    @RequestMapping(value = "/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam int cid, HttpServletRequest request) {
        String result = articleService.deleteByCid(cid);
        logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid + "", request.getRemoteAddr(), this.getUid(request));
        if (!WebConstant.SUCCESS_RESULT.equals(result)) {
            return ApiResponse.fail(result);
        }
        return ApiResponse.ok();
    }
}
