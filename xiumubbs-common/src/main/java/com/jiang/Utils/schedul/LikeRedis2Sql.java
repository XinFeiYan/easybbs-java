package com.jiang.Utils.schedul;

import com.jiang.Utils.RedisUtil;
import com.jiang.entity.po.ForumArticle;
import com.jiang.entity.vo.ForumArticleVO;
import com.jiang.mapper.ForumArticleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Component
public class LikeRedis2Sql {
    private static final Logger logger =  LoggerFactory.getLogger(LikeRedis2Sql.class);

    @Resource
    private ForumArticleDao<ForumArticle, ForumArticleVO> forumArticleDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0 */10 * * * *")
    public void doLike2Sql(){
        logger.info("定时同步文章点赞信息 - " + new Date()+"- 开始");
        Map<String, Integer> articleLikesMap = redisTemplate.opsForHash().entries(RedisUtil.getArticleLikeCount());

        // 遍历每个文章ID及其点赞数，写入数据库
        for (Map.Entry<String, Integer> entry : articleLikesMap.entrySet()) {
            String articleId = entry.getKey();
            Integer likeCount = entry.getValue();
             // 假设点赞数存储为字符串，这里转换为整数

            // 更新数据库中对应文章的点赞数
            ForumArticle forumArticle = forumArticleDao.selectByArticleId(articleId);
            if (forumArticle != null) {
                ForumArticle article = new ForumArticle();
                article.setGoodCount(likeCount);
                forumArticleDao.updateByArticleId(article,articleId);
            } else {
                logger.warn("文章ID " + articleId + " 在数据库中不存在，无法更新点赞数。");
            }
        }

        logger.info("定时同步文章点赞信息 - " + new Date()+"- 结束");
    }
}
