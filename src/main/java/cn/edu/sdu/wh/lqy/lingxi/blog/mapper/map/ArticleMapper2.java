package cn.edu.sdu.wh.lqy.lingxi.blog.mapper.map;

import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.Article;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ArticleMapper2 extends BaseMapper<Article> {

    long countByTypeAndStatus(String type, String status);

    List<Article> findByCatalog(@Param("value") int value);

}
