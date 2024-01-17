package top.qxfly.service.Article.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.qxfly.entity.Article;
import top.qxfly.mapper.Article.ArticleMapper;
import top.qxfly.pojo.PageBean;
import top.qxfly.pojo.Result;
import top.qxfly.service.Article.ArticleService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public PageBean<Article> getArticlesByPage(int currPage, int pageSize, String searchData, String token) {
        int count = articleMapper.getArticleCount(searchData, token);
        PageBean<Article> pageBean = new PageBean<>(currPage, pageSize, count);
        List<Article> articleList = articleMapper.getArticlesByPage(pageBean.getStart(), pageSize, searchData, token);
        pageBean.setData(articleList);
        return pageBean;
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
        String suffix = split1[split1.length - 1];
        String fileName = uuid + "." + suffix;
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
            return Result.success(articleImageDownloadPath + fileName);
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
            boolean delete = cover.delete();
            if (delete) {
                return true;
            } else {
                return false;
            }
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


}
