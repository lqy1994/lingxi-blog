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

    private Integer wordCnt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWordCnt() {
        return wordCnt;
    }

    public void setWordCnt(Integer wordCnt) {
        this.wordCnt = wordCnt;
    }

}
