package top.qxfly.mapper.Article;

import org.apache.ibatis.annotations.*;
import top.qxfly.entity.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {
    /**
     * 发布文章
     *
     * @param article
     * @return
     */
    @Insert("insert into article(title, content, preview, updateTime, createTime, tag, likes, views, authorId, author, cover)VALUES (#{title},#{content},#{preview},#{updateTime},#{createTime},#{tag},#{likes},#{views},#{authorId},#{author},#{cover})")
    boolean releaseArticle(Article article);

    /**
     * 获取文章数
     *
     * @return
     */
    int getArticleCount(String searchData, String token);

    /**
     * 分页获取文章
     *
     * @param start
     * @param pageSize
     * @return
     */
    List<Article> getArticlesByPage(int start, int pageSize, String searchData, String token);

    /**
     * 根据id获取文章
     *
     * @param id
     * @return
     */
    @Select("select * from article where id = #{id}")
    Article getArticleById(int id);

    /**
     * 编辑文章
     *
     * @param article
     * @return
     */
    @Update("update article set title = #{title}, content = #{content}, preview = #{preview}, cover = #{cover}, updateTime = #{updateTime}, tag = #{tag} where id = #{id}")
    boolean editArticle(Article article);

    /**
     * 删除文章
     *
     * @param article
     * @return
     */
    @Delete("delete from article where id = #{id}")
    boolean deleteArticleById(Article article);
}
