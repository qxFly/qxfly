package fun.qxfly.service.Article.Impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import fun.qxfly.entity.*;
import fun.qxfly.mapper.Article.ArticleMapper;
import fun.qxfly.pojo.PageBean;
import fun.qxfly.pojo.Result;
import fun.qxfly.service.Article.ArticleService;
import fun.qxfly.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Value("${file.articleCover.download.path}")
    private String articleCoverDownloadPath;
    @Value("${file.articleImage.download.path}")
    private String articleImageDownloadPath;
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @Override
    public boolean releaseArticle(Article article) {
        return articleMapper.releaseArticle(article);
    }

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean daily, int authorId, int verify, String classify, String[] tagArr) {
        int count;
        if (!daily) {
            count = articleMapper.getArticleCount(searchData, authorId, verify, classify, tagArr);
        } else {
            count = articleMapper.getDailyArticleCount();
        }
        PageBean<ArticleVO> pageBean = new PageBean<>(currPage, pageSize, count);
        List<ArticleVO> articleList;
        if (!daily) {
            articleList = articleMapper.getArticlesByPage(pageBean.getStart(), pageSize, searchData, sort, authorId, verify, classify, tagArr);
        } else {
            articleList = articleMapper.getDailyArticlesByPage(pageBean.getStart(), pageSize);
        }
        pageBean.setData(articleList);
        return pageBean;
    }

    /**
     * 分页获取收藏文章
     *
     * @param currPage
     * @param pageSize
     * @param searchData
     * @param sort
     * @param uid
     * @return
     */
    @Override
    public PageInfo<ArticleVO> getCollectionArticles(int currPage, int pageSize, String searchData, String sort, int uid) {
        PageHelper.startPage(currPage, pageSize);
        List<ArticleVO> articleList = articleMapper.getUserCollection(searchData, sort, uid);
        return new PageInfo<>(articleList);
    }

    /**
     * 删除文章图片
     *
     * @param imageList
     * @return
     */
    @Override
    public boolean deleteArticleImage(String[] imageList) {
        String path = System.getProperty("user.dir") + "/data/qxfly-articleImage/";
        for (String item : imageList) {
            File file = new File(path + item);
            if (file.exists()) {
                if (!file.delete()) return false;
            }
        }
        return true;
    }

    /**
     * 增加文章访问量
     *
     * @param articleId
     * @param userId
     */
    @Override
    public void addArticleView(Integer articleId, Integer userId, String UA) {

        Integer view = articleMapper.getUserArticleView(articleId, userId, UA);
        if (view == null || view == 0) {
            articleMapper.addUserArticleView(articleId, userId, UA);
            articleMapper.addArticleTotalViews(articleId);
            DailyView dailyView = articleMapper.getDailyViewByArticleId(articleId);
            if (dailyView == null) {
                articleMapper.addDailyView(articleId);
            } else {
                articleMapper.updateDailyView(articleId);
            }
        }
    }

    /**
     * 清空用户每日浏览记录
     *
     * @return
     */
    @Override
    public boolean resetUserDailyViewTask() {
        return articleMapper.resetUserDailyViewTask();

    }

    /**
     * 清空用户每日点赞记录
     *
     * @return
     */
    @Override
    public boolean resetUserDailyLikeTask() {
        return articleMapper.resetUserDailyLikeTask();
    }

    /**
     * 分页获取所有分类
     *
     * @return
     */
    @Override
    public PageInfo<Classify> listClassifiesByPage(int currPage, int pageSize, Integer id, String name) {
        PageHelper.startPage(currPage, pageSize);
        List<Classify> classifyList = articleMapper.listClassifiesByPage(id, name);
        return new PageInfo<>(classifyList);
    }

    /**
     * 获取所有分类
     *
     * @return
     */
    @Override
    public List<Classify> listClassifies() {
        return articleMapper.listClassifies();
    }

    /**
     * 分页获取所有标签
     *
     * @param currPage
     * @param pageSize
     * @param id
     * @param name
     * @param uid
     * @return
     */
    @Override
    public PageInfo<Tag> listTagsByPage(Integer currPage, Integer pageSize, Integer id, String name, Integer uid) {
        PageHelper.startPage(currPage, pageSize);
        List<Tag> tagList = articleMapper.listTagsByPage(id, name, uid);
        return new PageInfo<>(tagList);
    }

    /**
     * 获取所有标签
     *
     * @return
     */
    @Override
    public List<Tag> listTags() {
        return articleMapper.listTags();
    }

    @Override
    public boolean likeComment(Comment comment, User user) {
        // 0 为点赞，1为取消点赞
        if(comment.getLikeCount() == 0){
            Integer i = articleMapper.getUserCommentLike(user, comment);
            // 用户没有点过赞
            if (i == null || i == 0) {
                articleMapper.addUserCommentLike(user, comment);
            }
            Integer userCommentDailyLike = articleMapper.getUserCommentDailyLike(user, comment);
            if (userCommentDailyLike == null || userCommentDailyLike == 0){
                articleMapper.addUserCommentDailyLike(user, comment);
                articleMapper.addCommentLike(comment);
            }
        }else{
            articleMapper.cancelUserCommentLike(user, comment);
        }
        return true;
    }

    /**
     * 获取用户点赞的评论
     * @param aid
     * @param uid
     * @return
     */
    @Override
    public List<Integer> getUserLikeComment(Integer aid, int uid) {
        return articleMapper.getUserLikeComment(aid, uid);
    }

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Override
    public Article getArticleById(int id) {
        return articleMapper.getArticleById(id);
    }

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @Override
    public Result updateArticleCover(MultipartFile file) {
        String path = System.getProperty("user.dir") + "/data/qxfly-articleCover";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        String[] split1 = file.getOriginalFilename().split("\\.");
        String fileName = uuid + "." + "webp";
        try (OutputStream outputStream = new FileOutputStream(path + "/" + fileName)) {
            outputStream.write(file.getBytes());
            return Result.success(articleCoverDownloadPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 文章内容图片上传
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadArticleImage(MultipartFile file) {
        String path = System.getProperty("user.dir") + "/data/qxfly-articleImage";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
        String[] split1 = file.getOriginalFilename().split("\\.");
        String suffix = split1[split1.length - 1];
        String fileName = uuid + "." + suffix;
        try (OutputStream outputStream = new FileOutputStream(path + "/" + fileName)) {
            outputStream.write(file.getBytes());
            return Result.success(split1[0], articleImageDownloadPath + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @Override
    public boolean deleteArticle(Article article) {
        String s = article.getCover().split("/")[article.getCover().split("/").length - 1];
        File cover = new File(System.getProperty("user.dir") + "/data/qxfly-articleCover/" + s);
        if (cover.exists()) cover.delete();
        return articleMapper.deleteArticleById(article);
    }

    /**
     * 删除之前的封面
     *
     * @param coverUrl
     * @return
     */
    @Override
    public boolean deletePreviousCover(String coverUrl) {
        String[] split = coverUrl.split("/");
        String coverName = split[split.length - 1];
        String path = System.getProperty("user.dir") + "/data/qxfly-articleCover/";
        File cover = new File(path + coverName);
        if (cover.exists()) {
            return cover.delete();
        } else {
            return true;
        }
    }

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Override
    public boolean editArticle(Article article) {
        return articleMapper.editArticle(article);
    }

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    @Override
    public PageBean<Comment> getArticleCommentsByPage(int currPage, int pageSize, String sort, int id) {
        int count = articleMapper.getArticleCommentsCount(id);
        PageBean<Comment> pageBean = new PageBean<>(currPage, pageSize, count);
        List<Comment> comments = articleMapper.getArticleCommentsByPage(pageBean.getStart(), pageSize, sort, id);
        for (Comment comment : comments) {
            List<Comment> child = articleMapper.getChildCommentByCommentId(comment.getId());
            comment.setChildComment(child);
        }
        pageBean.setData(comments);
        return pageBean;
    }

    /**
     * 发布评论
     *
     * @param comment
     * @return
     */
    @Override
    public boolean releaseComment(Comment comment) {
        boolean matches = comment.getContent().matches("傻逼|王八蛋|卖淫|嫖娼|赌博|吸食|毒品|装逼|草泥马|特么的|撕逼|玛勒戈壁|爆菊|JB|呆逼|本屌|齐B短裙|法克鱿|丢你老母|吉跋猫|妈蛋|逗比|我靠|碧莲|碧池|然并卵|日了狗|屁民|吃翔|XX你老母|达菲鸡|装13|逼格|蛋疼|傻逼|绿茶婊|你妈的|表砸|屌爆了|买了个表|淫家|你妹|浮尸国|滚粗");
        if(!matches){
            comment.setVerify(3);
        }else{
            comment.setVerify(1);
        }
        return articleMapper.releaseComment(comment);
    }

    /**
     * 清空每日浏览量
     *
     * @return
     */
    @Override
    public boolean resetDailyViewTask(String type) {
        if (type.equals("daily")) {
            return articleMapper.resetDailyView();
        } else if (type.equals("weekly")) {
            return articleMapper.resetWeeklyViews();
        } else {
            return articleMapper.resetMonthlyViews();
        }
    }

    /**
     * 文章点赞
     *
     * @param articleId
     * @return
     */
    @Override
    public boolean articleLike(Integer articleId, Integer userId) {
        Integer al = articleMapper.getUserArticleLike(articleId, userId);

        /*获取用户的点赞json数据*/
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(userId);
        ArrayList<Integer> articles = new ArrayList<>();
        /*如果为空，则创建相关json数据*/
        if (userLikes == null) {
            articles.add(articleId);
            UserLikesAndCollection userLikes1 = new UserLikesAndCollection(userId, JSONObject.toJSONString(articles), JSONObject.toJSONString(""));
            articleMapper.addUserLikes(userLikes1);
        } else {
            //否则查询用户是否点赞
            String likeArticles = userLikes.getLikeArticles();
            ArrayList<Integer> arrayList = new ArrayList<>();
            if (likeArticles != null) {
                arrayList = JSONObject.parseObject(likeArticles, ArrayList.class);

                for (Object item : arrayList) {
                    if (item.equals(articleId)) {
                        //已点赞
                        return false;
                    }
                }
                //如果点赞记录超过500条，则删除最早的一条
                if (arrayList.size() > 500) {
                    arrayList.remove(0);
                }
            }
            //未点赞
            arrayList.add(articleId);
            userLikes.setLikeArticles(JSONObject.toJSONString(arrayList));
            articleMapper.updateUserLikes(userLikes);
            if (al == null || al == 0) {
                articleMapper.addUserArticleLike(articleId, userId);
                articleMapper.articleLike(articleId);
            }

        }
        return true;
    }

    /**
     * 取消用户点赞
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public boolean cancelArticleLike(Integer articleId, Integer userId) {
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(userId);
        if (userLikes != null && userLikes.getLikeArticles() != null) {
            ArrayList<Integer> arrayList = JSONObject.parseObject(userLikes.getLikeArticles(), ArrayList.class);
            for (Integer item : arrayList) {
                if (item.equals(articleId)) {
                    arrayList.remove(articleId);
                    userLikes.setLikeArticles(JSONObject.toJSONString(arrayList));
                    articleMapper.updateUserLikes(userLikes);
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 文章收藏
     *
     * @param articleId
     * @param userId
     */
    @Override
    public boolean articleCollection(Integer articleId, Integer userId) {
        Integer b = articleMapper.userIsCollArt(articleId, userId);
        if (b == null) {
            articleMapper.updateUserCollection(articleId, userId, new Date());
            articleMapper.addarticleCollectionCount(articleId);
        }
        return true;
    }

    /**
     * 取消用户收藏
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public boolean cencelArticleCollection(Integer articleId, Integer userId) {
        return articleMapper.deleteUserCollection(articleId, userId);
    }

    /**
     * 判断文章是否点赞收藏
     *
     * @param articleId
     * @param userId
     */
    @Override
    public Result
    isArticleLike(Integer articleId, Integer userId) {
        String like = "false";
        String collection = "false";
        UserLikesAndCollection userLikes = articleMapper.getUserLikes(userId);
        if (userLikes == null) {
            return Result.success("false", "false");
        } else {
            if (userLikes.getLikeArticles() != null) {
                String likeArticles = userLikes.getLikeArticles();
                ArrayList<Integer> a = JSONObject.parseObject(likeArticles, ArrayList.class);
                for (Object item : a) {
                    if (item.equals(articleId)) {
                        like = "true";
                        break;
                    }
                }
            }
            if (articleMapper.userIsCollArt(articleId, userId) != null) collection = "true";
        }
        return Result.success(like, collection);
    }
}
