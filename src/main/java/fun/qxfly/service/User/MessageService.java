package fun.qxfly.service.User;

import fun.qxfly.entity.Message;
import fun.qxfly.vo.MessageNavVO;

import java.util.List;

public interface MessageService {

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    String sendMessage(Message message);

    /**
     * 读取消息
     *
     * @param message
     * @return
     */
    boolean readMessage(Message message);

    /**
     * 获取指定用户发送的消息
     *
     * @param msgId
     * @param size
     * @return
     */
    List<Message> listMessage(String msgId, Integer size);

    /**
     * 获取给当前用户发送的消息的用户
     * @param uid
     * @param size
     * @return
     */
    List<MessageNavVO> listMessageUserNav(Integer uid, Integer size);

    /**
     * 初始化消息对话
     * @param uid
     * @return
     */
    MessageNavVO initUserMessage(Integer uid);

    /**
     * 查看是否有未读消息
     * @param userId
     * @return
     */
    Integer getNoReadMessageCount(Integer userId);
}
