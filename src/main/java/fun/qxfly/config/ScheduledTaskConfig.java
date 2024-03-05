package fun.qxfly.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import fun.qxfly.service.Article.ArticleService;
import fun.qxfly.service.User.UserInfoService;

@Slf4j
@Configuration
public class ScheduledTaskConfig {
    final ArticleService articleService;
    final UserInfoService userInfoService;

    public ScheduledTaskConfig(ArticleService articleService, UserInfoService userInfoService) {
        this.articleService = articleService;
        this.userInfoService = userInfoService;
    }

    /**
     * 清空每日浏览量,和用户每日浏览记录
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyViewTask() {
        log.info("清空每日浏览量,和用户每日浏览记录");
        if (articleService.resetDailyViewTask("daily")) {
            log.info("每日浏览量重置完成");
        } else {
            log.info("每日浏览量重置出错");
        }
        if (articleService.resetUserDailyViewTask()) {
            log.info("用户每日浏览记录量重置完成");
        } else {
            log.info("用户每日浏览记录重置出错");
        }
        if (articleService.resetUserDailyLikeTask()) {
            log.info("用户每日点赞记录量重置完成");
        } else {
            log.info("用户每日点赞记录重置出错");
        }
    }

    /**
     * 清空每周浏览量
     */
    @Scheduled(cron = "0 0 0 ? * 7")
    public void resetWeeklyViewTask() {
        log.info("重置每周浏览量");
        if (articleService.resetDailyViewTask("weekly")) {
            log.info("每周浏览量重置完成");
        } else {
            log.info("重置出错");
        }
    }

    /**
     * 清空每月浏览量
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void resetMonthlyViewTask() {
        log.info("重置每月浏览量");
        if (articleService.resetDailyViewTask("monthly")) {
            log.info("每月浏览量重置完成");
        } else {
            log.info("重置出错");
        }
    }

    /**
     * 刷新用户信息
     */
//    @Scheduled(fixedDelay = 5000)
    @Scheduled(fixedDelay = 1000 * 60 * 60)
    public void refreshUserInfoTask() {
        log.info("刷新用户信息");
        Integer[] count = userInfoService.refreshUserInfoTask();
        log.info("共 {} 个用户, 已刷新 {} 个用户的信息", count[0], count[1]);
    }

    /**
     * 定时清理超时验证码
     */
//    @Scheduled(fixedDelay = 5000)
    @Scheduled(fixedDelay = 1000 * 60 * 2)//2分钟
    public void clearCaptcha() {
        log.info("清除验证码");
        Integer[] count = userInfoService.clearCaptchaTask();
    }
}
