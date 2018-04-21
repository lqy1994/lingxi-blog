package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.MetaVoMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.dto.Types;
import cn.edu.sdu.wh.lqy.lingxi.blog.exception.LingXiException;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.ContentVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IMetaService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IRelationshipService;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.DateKit;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.TaleUtils;
import cn.edu.sdu.wh.lqy.lingxi.blog.utils.Tools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ArticleServiceImpl implements IArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MetaVoMapper metaVoMapper;

    @Autowired
    private IRelationshipService relationshipService;

    @Autowired
    private IMetaService metasService;

    @Override
    @Transactional
    public String publish(Article contents) {
        if (null == contents) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(contents.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = contents.getTitle().length();
        if (titleLength > WebConstant.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = contents.getContent().length();
        if (contentLength > WebConstant.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == contents.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isNotBlank(contents.getSlug())) {
            if (contents.getSlug().length() < 5) {
                return "路径太短了";
            }
            if (!TaleUtils.isPath(contents.getSlug())) return "您输入的路径不合法";
            ContentVoExample contentVoExample = new ContentVoExample();
            contentVoExample.createCriteria().andTypeEqualTo(contents.getType()).andStatusEqualTo(contents.getSlug());
            long count = articleMapper.countByExample(contentVoExample);
            if (count > 0) return "该路径已经存在，请重新输入";
        } else {
            contents.setSlug(null);
        }

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contents.setCreated(time);
        contents.setModified(time);
        contents.setHits(0);
        contents.setCommentsNum(0);

        String tags = contents.getTags();
        String categories = contents.getCategories();
        articleMapper.insert(contents);
        Integer cid = contents.getCid();
        metasService.saveMetas(cid, tags, Types.TAG.getType());
        metasService.saveMetas(cid, categories, Types.CATEGORY.getType());
        return WebConstant.SUCCESS_RESULT;
    }

    @Override
    public PageInfo<Article> getContents(Integer offset, Integer limit) {
        LOGGER.debug("Begin getContents: offset:{}, limit:{}.", offset, limit);
        ContentVoExample example = new ContentVoExample();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(Types.ARTICLE.getType()).andStatusEqualTo(Types.PUBLISH.getType());
        PageHelper.startPage(offset, limit);
        List<Article> data = articleMapper.selectByExampleWithBLOBs(example);
        PageInfo<Article> pageInfo = new PageInfo<>(data);
        LOGGER.debug("Exit getContents");
        return pageInfo;
    }

    @Override
    public Article getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                Article article = articleMapper.selectByPrimaryKey(Integer.valueOf(id));
                if (article != null) {
                    article.setHits(article.getHits() + 1);
                    articleMapper.updateByPrimaryKey(article);
                }
                return article;
            } else {
                ContentVoExample contentVoExample = new ContentVoExample();
                contentVoExample.createCriteria().andSlugEqualTo(id);
                List<Article> articles = articleMapper.selectByExampleWithBLOBs(contentVoExample);
                if (articles.size() != 1) {
                    throw new LingXiException("query content by id and return is not one");
                }
                return articles.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateContentByCid(Article article) {
        if (null != article && null != article.getCid()) {
            articleMapper.updateByPrimaryKeySelective(article);
        }
    }

    @Override
    public PageInfo<Article> getArticles(Integer mid, int page, int limit) {
        int total = metaVoMapper.countWithSql(mid);
        PageHelper.startPage(page, limit);
        List<Article> list = articleMapper.findByCatalog(mid);
        PageInfo<Article> paginator = new PageInfo<>(list);
        paginator.setTotal(total);
        return paginator;
    }

    @Override
    public PageInfo<Article> getArticles(String keyword, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        ContentVoExample contentVoExample = new ContentVoExample();
        ContentVoExample.Criteria criteria = contentVoExample.createCriteria();
        criteria.andTypeEqualTo(Types.ARTICLE.getType());
        criteria.andStatusEqualTo(Types.PUBLISH.getType());
        criteria.andTitleLike("%" + keyword + "%");
        contentVoExample.setOrderByClause("created desc");
        List<Article> articles = articleMapper.selectByExampleWithBLOBs(contentVoExample);
        return new PageInfo<>(articles);
    }

    @Override
    public PageInfo<Article> getArticlesWithpage(ContentVoExample commentVoExample, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<Article> articles = articleMapper.selectByExampleWithBLOBs(commentVoExample);
        return new PageInfo<>(articles);
    }

    @Override
    @Transactional
    public String deleteByCid(Integer cid) {
        Article contents = this.getContents(cid + "");
        if (null != contents) {
            articleMapper.deleteByPrimaryKey(cid);
            relationshipService.deleteById(cid, null);
            return WebConstant.SUCCESS_RESULT;
        }
        return "数据为空";
    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {
        Article article = new Article();
        article.setCategories(newCatefory);
        ContentVoExample example = new ContentVoExample();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        articleMapper.updateByExampleSelective(article, example);
    }

    @Override
    @Transactional
    public String updateArticle(Article contents) {
        if (null == contents) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(contents.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = contents.getTitle().length();
        if (titleLength > WebConstant.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = contents.getContent().length();
        if (contentLength > WebConstant.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == contents.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isBlank(contents.getSlug())) {
            contents.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contents.setModified(time);
        Integer cid = contents.getCid();
        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        articleMapper.updateByPrimaryKeySelective(contents);
        relationshipService.deleteById(cid, null);
        metasService.saveMetas(cid, contents.getTags(), Types.TAG.getType());
        metasService.saveMetas(cid, contents.getCategories(), Types.CATEGORY.getType());
        return WebConstant.SUCCESS_RESULT;
    }
}
