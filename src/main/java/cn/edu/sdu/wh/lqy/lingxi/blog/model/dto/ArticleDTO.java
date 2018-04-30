package cn.edu.sdu.wh.lqy.lingxi.blog.model.dto;

import java.util.Date;

public class ArticleDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 内容标题
     */
    private String title;

    /**
     * 内容生成时的GMT unix时间戳
     */
    private Date createTime;

    /**
     * 内容更改时的GMT unix时间戳
     */
    private Date modifyTime;

    /**
     * 内容所属用户id
     */
    private Integer authorId;

    private String status;

    private String tags;

    private String categories;

    private Integer hits;

    private Integer commentsNum;

    private String content;

//    private

}
