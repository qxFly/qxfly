package top.qxfly.mapper.Article;

import org.apache.ibatis.annotations.*;
import top.qxfly.entity.Article;
import top.qxfly.entity.Comment;

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
    List<Article> getArticlesByPage(int start, int pageSize, String searchData, String sort, String token);

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

    /**
     * 获取文章评论数
     * @param id
     * @return
     */
    @Select("select count(*) from comment where articleId = #{id} and parentCommentId = 0")
    int getArticleCommentsCount(int id);

    /**
     * 根据文章id获取评论
     *
     * @param id
     * @return
     */
    List<Comment> getArticleCommentsByPage(int start, int pageSize,String sort, int id);

    /**
     * 获取子评论
     * @param id
     * @return
     */
    @Select("select c.*,u.avatar from comment c join `user` u on u.id = c.userId where parentCommentId = #{id} order by c.createTime ASC")
    List<Comment> getChildCommentByCommentId(Integer id);

    /**
     * 发布评论
     * @param comment
     * @return
     */
    @Insert("insert into comment(articleId, content, parentCommentId, userId, username, createTime,toUserId,toUsername)values(#{articleId},#{content},#{parentCommentId},#{userId},#{username},#{createTime},#{toUserId},#{toUsername})")
    boolean releaseComment(Comment comment);
}
