package fun.qxfly.mapper.User;

import fun.qxfly.entity.Message;
import fun.qxfly.vo.MessageNavVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    @Insert("insert into message (uid2, uid1, content, sendTime, msgId) values(#{uid2}, #{uid1}, #{content}, #{sendTime}, #{msgId})")
    boolean sendMessage(Message message);

    /**
     * 读取消息
     *
     * @param message
     * @return
     */
    @Update("update message set status = 1 where msgId = #{msgId} and uid2 = #{uid2}")
    boolean readMessage(Message message);

    /**
     * 获取指定用户发送的消息
     *
     * @param msgId
     * @param size
     * @return
     */
    @Select("select * from message where msgId = #{msgId} order by sendTime limit #{size}")
    List<Message> listMessage(String msgId, Integer size);

    /**
     * 获取给当前用户发送的消息的用户
     *
     * @param uid
     * @param size
     * @return
     */
    @Select("select id, username, avatar from user where (id in (select distinct uid2 from message where uid1 = #{uid} order by sendTime desc)) or (id in (select distinct uid1 from message where uid2 = #{uid} order by sendTime desc)) limit #{size}")
    List<MessageNavVO> listMessageUserNav(Integer uid, Integer size);

    /**
     * 获取对话id
     *
     * @param uid
     * @param fromUid
     * @return
     */
    @Select("select distinct msgId from message where (uid1 = #{uid} and uid2 = #{fromUid} )or (uid2 = #{uid} and uid1 = #{fromUid})")
    String getMsgId(Integer uid, Integer fromUid);

    /**
     * 初始化消息对话
     *
     * @param uid
     * @return
     */
    @Select("select id, username, avatar from user where id = #{uid}")
    MessageNavVO initUserMessage(Integer uid);

    /**
     * 根据对话id获取未读消息数量
     *
     * @param msgId
     * @return
     */
    @Select("select count(*) from message where msgId = #{msgId} and uid2 = #{uid} and status = 0")
    Integer getNoReadCountByMsgId(Integer uid, String msgId);

    /**
     * 根据用户id查看是否有未读消息
     *
     * @param userId
     * @return
     */
    @Select("select count(*) from message where uid2 = #{userId} and status = 0")
    Integer getNoReadCountByUid(Integer userId);
}
