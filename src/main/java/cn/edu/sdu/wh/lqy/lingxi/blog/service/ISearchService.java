package cn.edu.sdu.wh.lqy.lingxi.blog.service;

import cn.edu.sdu.wh.lqy.lingxi.blog.modal.Vo.ArticleVoSearch;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.search.ServiceMultiResult;
import cn.edu.sdu.wh.lqy.lingxi.blog.modal.search.ServiceResult;

import java.util.List;

public interface ISearchService {
    /**
     * 索引目标文章
     * @param artId
     */
    void index(Integer artId);

    /**
     * 移除文章索引
     * @param artId
     */
    void remove(Integer artId);

    /**
     * 查询文章接口
     * @param
     * @return
     */
    ServiceMultiResult<Integer> query(ArticleVoSearch articleVoSearch);

    /**
     * 获取补全建议关键词
     */
    ServiceResult<List<String>> suggest(String prefix);

}
