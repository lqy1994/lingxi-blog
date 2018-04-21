package cn.edu.sdu.wh.lqy.lingxi.blog.controller.web;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.RestPageConst;
import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.controller.BaseController;
import cn.edu.sdu.wh.lqy.lingxi.blog.dto.Types;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Bo.ArchiveBo;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Bo.CommentBo;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Meta;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ICommentService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IMetaService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ISiteService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 顶部菜单
 */
@Controller
public class HeadController extends BaseController {

    @Autowired
    private IMetaService metaService;
    @Autowired
    private ISiteService siteService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 归档页
     *
     * @return
     */
    @GetMapping(value = "archives")
    public String archives(Model model) {
        List<ArchiveBo> archives = siteService.getArchives();
        model.addAttribute("archives", archives);
        return RestPageConst.ARCHIVES;
    }

    /**
     * 友链页
     *
     * @return
     */
    @GetMapping(value = "links")
    public String links(Model model) {
        List<Meta> links = metaService.getMetas(Types.LINK.getType());
        model.addAttribute("links", links);
        return RestPageConst.LINKS;
    }

    /**
     * 自定义页面,如关于的页面
     */
    @GetMapping(value = "/{pageName}")
    public String page(Model model, @PathVariable String pageName, HttpServletRequest request) {
        Article article = articleService.getContents(pageName);
        if (article == null) {
            return this.render_404();
        }
        if (article.getAllowComment()) {
            String cp = request.getParameter("cp");
            if (StringUtils.isBlank(cp)) {
                cp = "1";
            }
            PageInfo<CommentBo> commentsPaginator = commentService.getComments(article.getId(), Integer.parseInt(cp), 6);
            model.addAttribute("comments", commentsPaginator);
        }
        model.addAttribute("article", article);
        updateArticleHit(article.getId(), article.getHits());
        return RestPageConst.PAGES;
    }

    private void updateArticleHit(Integer articleId, Integer chits) {
        String hitStr = redisTemplate.opsForValue().get("article" + ":" + "hits");
        Integer hits = 0;
        if (cn.edu.sdu.wh.lqy.lingxi.blog.utils.StringUtils.isNotNull(hitStr)) {
            hits = Integer.valueOf(hitStr);
        }

        if (chits == null) {
            chits = 0;
        }

        hits++;

        if (hits >= WebConstant.HIT_EXCEED) {
            Article temp = new Article();
            temp.setId(articleId);
            temp.setHits(chits + hits);
            articleService.updateContentByCid(temp);
            redisTemplate.opsForValue().set("article" + ":" + "hits", 1 + "");
        } else {
            redisTemplate.opsForValue().set("article" + ":" + "hits", hits + "");
        }
    }

}
