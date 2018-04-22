package cn.edu.sdu.wh.lqy.lingxi.blog.mapper;

import cn.edu.sdu.wh.lqy.lingxi.blog.model.Bo.ArchiveBo;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.ContentVoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ArticleMapper /*extends BaseMapper<Article>*/ {

    long countByExample(ContentVoExample example);

    int deleteByExample(ContentVoExample example);

    int deleteByPrimaryKey(Integer id);

    int insertNewArticle(Article article);

    int insertSelective(Article record);

    List<Article> selectByExampleWithBLOBs(ContentVoExample example);

    List<Article> selectByExample(ContentVoExample example);

    Article selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Article record, @Param("example") ContentVoExample example);

    int updateByExampleWithBLOBs(@Param("record") Article record, @Param("example") ContentVoExample example);

    int updateByExample(@Param("record") Article record, @Param("example") ContentVoExample example);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKeyWithBLOBs(Article record);

    int updateByPrimaryKey(Article record);

    List<ArchiveBo> findReturnArchiveBo();

    List<Article> findByCatalog(Integer mid);

}