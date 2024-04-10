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
    @Value("${file.articleAttachment.download.path}")
    private String articleAttachmentDownloadPath;
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
    public Integer releaseArticle(Article article, String image) {
        /*封面以文件名方式保存至数据库*/
        String[] split1 = article.getCover().split("/");
        article.setCover(split1[split1.length - 1]);
        articleMapper.releaseArticle(article);
        /*保存文章中的图片*/
        if (image != null && !image.equals("")) {
            String[] imageArr = image.split(",");
            String[] imageUrl = new String[imageArr.length];
            for (int i = 0; i < imageArr.length; i++) {
                String[] split = imageArr[i].split("/");
                String imageName = split[split.length - 1];
                /*如果文章公开图片，则单独保存*/
                if (article.getPub() % 10 == 1)
                    articleMapper.savePubImage(article.getId(), imageName, new Date());
                else
                    articleMapper.removePubImage(article.getId());
                imageUrl[i] = imageName;
            }
            articleMapper.saveArticleImage(article.getId(), JSONObject.toJSONString(imageUrl));
        }
        return article.getId();
    }

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Override
    public boolean editArticle(Article article) {
        /*以文件名方式保存至数据库*/
        String[] split1 = article.getCover().split("/");
        article.setCover(split1[split1.length - 1]);
        /*保存文章中的图片*/
        String[] imageArr = article.getAuthor().split(",");
        String[] imageUrl = new String[imageArr.length];
        for (int i = 0; i < imageArr.length; i++) {
            String[] split = imageArr[i].split("/");
            String imageName = split[split.length - 1];
            /*如果文章公开图片，则单独保存*/
            if (article.getPub() % 10 == 1) {
                if (!imageName.equals("")) {
                    Integer c = articleMapper.checkSamePubImage(article.getId(), imageName);
                    if (c == 0)
                        articleMapper.savePubImage(article.getId(), imageName, new Date());
                }
            } else {
                articleMapper.removePubImage(article.getId());
            }
            imageUrl[i] = imageName;
        }
        articleMapper.updateArticleImage(article.getId(), JSONObject.toJSONString(imageUrl));
        return articleMapper.editArticle(article);
    }

    /**
     * 分页获取文章
     *
     * @param currPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<ArticleVO> getArticlesByPage(int currPage, int pageSize, String searchData, String sort, boolean daily, int authorId, int verify, String classify, String[] tagArr, int pub) {
        int count;
        if (!daily) {
            count = articleMapper.getArticleCount(searchData, authorId, verify, classify, tagArr, pub);
        } else {
            count = articleMapper.getDailyArticleCount();
        }
        PageBean<ArticleVO> pageBean = new PageBean<>(currPage, pageSize, count);
        List<ArticleVO> articleList;
        if (!daily) {
            articleList = articleMapper.getArticlesByPage(pageBean.getStart(), pageSize, searchData, sort, authorId, verify, classify, tagArr, pub);
        } else {
            articleList = articleMapper.getDailyArticlesByPage(pageBean.getStart(), pageSize);
        }
        for (ArticleVO article : articleList) {
            article.setCover(articleCoverDownloadPath + article.getCover());
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
        for (ArticleVO article : articleList) {
            article.setCover(articleCoverDownloadPath + article.getCover());
        }
        return new PageInfo<>(articleList);
    }

    /**
     * 编辑完成时删除没有选择的文章图片
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
                articleMapper.removePubImageByImageName(item);
                if (!file.delete()) return false;
            }
        }
        return true;
    }

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Override
    public Article getArticleById(int id) {
        Article article = articleMapper.getArticleById(id);
        article.setCover(articleCoverDownloadPath + article.getCover());
        return article;
    }

    /**
     * 文章封面上传
     *
     * @param file
     * @return
     */
    @Override
    public Result updateArticleCover(MultipartFile file) {
        //todo 文章封面上传 返回文件名字，而非地址（暂定）
        String path = System.getProperty("user.dir") + "/data/qxfly-articleCover";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        String uuid = UUID.randomUUID().toString();
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
        /* 删除封面 */
        String s = article.getCover().split("/")[article.getCover().split("/").length - 1];
        File cover = new File(System.getProperty("user.dir") + "/data/qxfly-articleCover/" + s);
        if (cover.exists()) cover.delete();
        /* 删除内容图片 */
        String images = articleMapper.getArticleImage(article);
        if (images != null) {
            ArrayList<String> arrayList = JSONObject.parseObject(images, ArrayList.class);
            String path = System.getProperty("user.dir") + "/data/qxfly-articleImage/";
            for (String image : arrayList) {
                File file = new File(path + image);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        /* 删除附件 */
        List<Attachment> attachmentList = articleMapper.getArticleAttachmentByAid(article.getId());
        for (Attachment attachment : attachmentList) {
            File file = new File(System.getProperty("user.dir") + "/data/qxfly-articleAttachment/" + attachment.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
        articleMapper.deleteAllArticleAttachmentByAid(article.getId());
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

    /**
     * 上传文章附件
     *
     * @param file
     * @return
     */
    @Override
    public String uploadAttachment(MultipartFile file) {
        String path = System.getProperty("user.dir") + "/data/qxfly-articleAttachment/";
        File filePath = new File(path);
        if (!filePath.exists()) {
            boolean mkdirs = filePath.mkdirs();
        }
        if (file != null) {
            /* 把文件从临时文件转存到指定目录 */
//            try {
                String uuid = UUID.randomUUID().toString();
                String[] split = file.getOriginalFilename().split("\\.");
                String suffix = split[split.length - 1];
                String fileName = uuid + "." + suffix;
//                file.transferTo(new File(path + fileName));
                try (FileOutputStream fileOutputStream = new FileOutputStream(path + fileName)) {
                    fileOutputStream.write(file.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return fileName;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
        } else {
            return null;
        }
    }

    /**
     * 删除文章附件
     *
     * @param fileName
     * @return
     */
    @Override
    public boolean deleteAttachment(Integer aid, String fileName) {
        File file = new File(System.getProperty("user.dir") + "/data/qxfly-articleAttachment/" + fileName);
        if (aid != null && aid != 0) {
            articleMapper.deleteAttachment(aid, fileName);
        }

        return file.exists() && file.delete();
    }

    /**
     * 保存文章附件
     *
     * @param aid
     * @param attachmentList
     * @return
     */
    @Override
    public Integer saveAttachment(Integer aid, Integer uid, List<Attachment> attachmentList) {
        List<Attachment> articleAttachment = getArticleAttachment(aid);
        for (Attachment attachment : attachmentList) {
            articleMapper.saveAttachment(aid, uid, attachment);
        }
        return null;
    }

    /**
     * 获取文章附件
     *
     * @param id
     * @return
     */
    @Override
    public List<Attachment> getArticleAttachment(Integer id) {
        List<Attachment> articleAttachment = articleMapper.getArticleAttachmentByAid(id);
        for (Attachment attachment : articleAttachment) {
            attachment.setDownloadUrl(articleAttachmentDownloadPath + attachment.getFileName());
        }
        return articleAttachment;
    }
}
