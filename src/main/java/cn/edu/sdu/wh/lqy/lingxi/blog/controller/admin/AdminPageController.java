package cn.edu.sdu.wh.lqy.lingxi.blog.controller.admin;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.RestPageConst;
import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.LogActions;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.Types;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Bo.ApiResponse;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ContentVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.User;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ILogService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("admin/page")
public class AdminPageController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminPageController.class);

    @Autowired
    private IArticleService articleService;

    @Autowired
    private ILogService logService;

    @GetMapping(value = "")
    public String index(HttpServletRequest request) {
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");
        contentVoExample.createCriteria().andTypeEqualTo(Types.PAGE.getType());
        PageInfo<Article> contentsPaginator = articleService.getArticlesWithpage(contentVoExample, 1, WebConstant.MAX_POSTS);
        request.setAttribute("articles", contentsPaginator);
        return RestPageConst.ADMIN_PAGE_LIST;
    }

    @GetMapping(value = "new")
    public String newPage(HttpServletRequest request) {
        return "admin/page_edit";
    }

    @GetMapping(value = "/{cid}")
    public String editPage(@PathVariable String cid, HttpServletRequest request) {
        Article contents = articleService.getContents(cid);
        request.setAttribute("contents", contents);
        return RestPageConst.ADMIN_PAGE_EDIT;
    }

    @PostMapping(value = "publish")
    @ResponseBody
    public ApiResponse publishPage(@RequestParam String title, @RequestParam String content,
                                   @RequestParam String status, @RequestParam("slug") String thumbnail,
                                   @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        User users = this.user(request);
        Article contents = new Article();
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setThumbnail(thumbnail);
        contents.setType(Types.PAGE.getType());
        if (null != allowComment) {
            contents.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contents.setAllowPing(allowPing == 1);
        }
        contents.setAuthorId(users.getUid());
        String result = articleService.publish(contents);
        if (!WebConstant.SUCCESS_RESULT.equals(result)) {
            return ApiResponse.fail(result);
        }
        return ApiResponse.ok();
    }

    @PostMapping(value = "modify")
    @ResponseBody
    public ApiResponse modifyArticle(@RequestParam Integer cid, @RequestParam String title,
                                     @RequestParam String content,
                                     @RequestParam String status, @RequestParam String slug,
                                     @RequestParam(required = false) Integer allowComment, @RequestParam(required = false) Integer allowPing, HttpServletRequest request) {

        User users = this.user(request);
        Article contents = new Article();
        contents.setId(cid);
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setThumbnail(slug);
        contents.setType(Types.PAGE.getType());
        if (null != allowComment) {
            contents.setAllowComment(allowComment == 1);
        }
        if (null != allowPing) {
            contents.setAllowPing(allowPing == 1);
        }
        contents.setAuthorId(users.getUid());
        String result = articleService.updateArticle(contents);
        if (!WebConstant.SUCCESS_RESULT.equals(result)) {
            return ApiResponse.fail(result);
        }
        return ApiResponse.ok();
    }

    @RequestMapping(value = "delete")
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
