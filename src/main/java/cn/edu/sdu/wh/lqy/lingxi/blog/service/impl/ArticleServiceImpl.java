package cn.edu.sdu.wh.lqy.lingxi.blog.service.impl;

import cn.edu.sdu.wh.lqy.lingxi.blog.constant.WebConstant;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.ArticleMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.mapper.MetaMapper;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ContentVoExample;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.browse.BrowseSearch;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.ArticleDTO;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.dto.Types;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.search.ServiceMultiResult;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IArticleService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IMetaService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.IRelationshipService;
import cn.edu.sdu.wh.lqy.lingxi.blog.service.ISearchService;
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


@Service("articleService")
public class ArticleServiceImpl /*extends ServiceImpl<ArticleMapper, Article> */implements IArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MetaMapper metaMapper;

    @Autowired
    private IRelationshipService relationshipService;

    @Autowired
    private IMetaService metasService;
    @Autowired
    private ISearchService searchService;

    @Override
    @Transactional
    public String publish(Article article) {
        if (null == article) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(article.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(article.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = article.getTitle().length();
        if (titleLength > WebConstant.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = article.getContent().length();
        if (contentLength > WebConstant.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == article.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isNotBlank(article.getThumbnail())) {
            if (article.getThumbnail().length() < 5) {
                return "路径太短了";
            }
            if (!TaleUtils.isPath(article.getThumbnail())) return "您输入的路径不合法";
            ContentVoExample contentVoExample = new ContentVoExample();
            contentVoExample.createCriteria().andTypeEqualTo(article.getType()).andStatusEqualTo(article.getThumbnail());
            long count = articleMapper.countByExample(contentVoExample);
            if (count > 0) return "该路径已经存在，请重新输入";
        } else {
            article.setThumbnail(null);
        }

//        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setContent(article.getContent());

        int time = DateKit.getCurrentUnixTime();
        article.setCreated(time);
        article.setModified(time);
        article.setHits(0);
        article.setCommentsNum(0);

        String tags = article.getTags();
        String categories = article.getCategories();
        articleMapper.insertNewArticle(article);
        Integer artId = article.getId();
        metasService.saveMetas(artId, tags, Types.TAG.getType());
        metasService.saveMetas(artId, categories, Types.CATEGORY.getType());

        searchService.index(artId);

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
//                if (articles.size() != 1) {
////                    throw new LingXiException("query content by id and return is not one");
////                }
                return articles.get(0);
            }
        }
        return null;
    }

    @Override
    public void updateContentByCid(Article article) {
        if (null != article && null != article.getId()) {
            articleMapper.updateByPrimaryKeySelective(article);
        }
    }

    @Override
    public PageInfo<Article> getArticles(Integer mid, int page, int limit) {
        int total = metaMapper.countWithSql(mid);
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
        Article article = this.getContents(cid + "");
        if (null != article) {
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
    public ServiceMultiResult<ArticleDTO> query(BrowseSearch browseSearch) {
        return null;
    }

    @Override
    @Transactional
    public String updateArticle(Article article) {
        if (null == article) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(article.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(article.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = article.getTitle().length();
        if (titleLength > WebConstant.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = article.getContent().length();
        if (contentLength > WebConstant.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == article.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isBlank(article.getThumbnail())) {
            article.setThumbnail(null);
        }
        int time = DateKit.getCurrentUnixTime();
        article.setModified(time);
        Integer id = article.getId();
        article.setContent(EmojiParser.parseToAliases(article.getContent()));

        articleMapper.updateByPrimaryKeySelective(article);
        relationshipService.deleteById(id, null);
        metasService.saveMetas(id, article.getTags(), Types.TAG.getType());
        metasService.saveMetas(id, article.getCategories(), Types.CATEGORY.getType());
        searchService.index(id);
        return WebConstant.SUCCESS_RESULT;
    }
}
