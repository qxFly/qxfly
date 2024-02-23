package top.qxfly.mapper.Admin;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import top.qxfly.entity.Article;

import java.util.List;

@Mapper
public interface ArticleManageMapper {
    /**
     * 查询所有文章
     * @return
     */
    List<Article> listArticle(int verify);

    /**
     * 文章审核
     * @param articleId
     * @param verify
     * @return
     */
    @Update("update article set verify = #{verify} where id = #{articleId}")
    boolean articleVerify(int articleId, int verify);

    /**
     * 搜索文章
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
    List<Article> searchArticle(Integer articleId, String title, String author, Integer authorId, String tag, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd, Integer verify);

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    @Delete("delete from article where id = #{articleId}")
    boolean deleteArticle(int articleId);
}
