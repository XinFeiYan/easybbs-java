package com.jiang.Utils;

public class RedisUtil {
    private static final String STRING = ":";
    private static final String ARTICLE_LIKE = "article:like";

    public static String getArticleLike(String articleId){
        return "article:"+articleId+":like";
    }

    public static String getArticleLikeCount(){
        return ARTICLE_LIKE;
    }
}
