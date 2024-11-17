package com.jiang.entity.vo;

public class ForumArticleDetailVO {
    private ForumArticleVO forumArticle;
    private ForumArticleAttachmentVO attachment;
    //点赞
    private Boolean haveLike = false;

    public ForumArticleVO getForumArticle() {
        return forumArticle;
    }

    public void setForumArticle(ForumArticleVO forumArticle) {
        this.forumArticle = forumArticle;
    }

    public ForumArticleAttachmentVO getAttachment() {
        return attachment;
    }

    public void setAttachment(ForumArticleAttachmentVO attachment) {
        this.attachment = attachment;
    }

    public Boolean getHaveLike() {
        return haveLike;
    }

    public void setHaveLike(Boolean haveLike) {
        this.haveLike = haveLike;
    }
}
