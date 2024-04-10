package fun.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import fun.qxfly.entity.Article;

import java.util.List;

@Mapper
public interface ArticleManageMapper {
    /**
     * 查询所有文章
     *
     * @return
     */
    List<Article> listArticle(int verify);

    /**
     * 文章审核
     * @param article
     * @return
     */
    @Update("update article set verify = #{verify} ,tag = #{tag} ,classify = #{classify} where id = #{id}")
    boolean articleVerify(Article article);

    /**
     * 搜索文章
     *
     * @param articleId
     * @param title
     * @param author
     * @param authorId
     * @param tag
     * @param createTimeStart
     * @param createTimeEnd
     * @param updateTimeStart
     * @param updateTimeEnd
     * @param verify
     * @return
     */
    List<Article> searchArticle(Integer articleId, String title, String author, Integer authorId, String tag, String classify, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify);

    /**
     * 删除文章
     *
     * @param articleId
     * @return
     */
    @Delete("delete from article where id = #{articleId}")
    boolean deleteArticle(int articleId);

    /**
     * 添加未通过文章的原因
     * @param article
     * @param reason
     */
    @Insert("insert into not_passed_article (aid, reason) values (#{article.id}, #{reason})")
    void addNoPassedArticle(Article article, String reason);

    /**
     * 删除未通过文章的原因
     * @param article
     */
    @Delete("delete from not_passed_article where aid = #{id}")
    void removeNoPassedArticle(Article article);
}
