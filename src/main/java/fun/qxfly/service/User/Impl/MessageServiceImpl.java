package fun.qxfly.service.User.Impl;

import fun.qxfly.entity.Message;
import fun.qxfly.mapper.User.MessageMapper;
import fun.qxfly.service.User.MessageService;
import fun.qxfly.vo.MessageNavVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Value("${file.userImg.download.path}")
    private String userAvatarPath;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
    @Override
    public String sendMessage(Message message) {
        String msgId = null;
        if (message.getMsgId() == null || "".equals(message.getMsgId())) {
            msgId = UUID.randomUUID().toString();
            message.setMsgId(msgId);
        }
        message.setSendTime(new Date());
        boolean b = messageMapper.sendMessage(message);
        if (b) {
            return msgId;
        } else {
            return "error";
        }

    }

    /**
     * 读取消息
     *
     * @param message
     * @return
     */
    @Override
    public boolean readMessage(Message message) {
        return messageMapper.readMessage(message);
    }

    /**
     * 获取指定用户发送的消息
     *
     * @param msgId
     * @param size
     * @return
     */
    @Override
    public List<Message> listMessage(String msgId, Integer size) {
        return messageMapper.listMessage(msgId, size);
    }

    /**
     * 获取给当前用户发送的消息的用户
     *
     * @param uid
     * @param size
     * @return
     */
    @Override
    public List<MessageNavVO> listMessageUserNav(Integer uid, Integer size) {
        List<MessageNavVO> userList = messageMapper.listMessageUserNav(uid, size);
        for (MessageNavVO user : userList) {
            if (user.getAvatar() != null)
                user.setAvatar(userAvatarPath + user.getAvatar());
            String msgId = messageMapper.getMsgId(uid, user.getId());
            Integer c = messageMapper.getNoReadCountByMsgId(uid, msgId);
            user.setNoReadCount(c);
            user.setMsgId(msgId);
        }
        return userList;
    }

    /**
     * 初始化消息对话
     *
     * @param uid
     * @return
     */
    @Override
    public MessageNavVO initUserMessage(Integer uid) {
        MessageNavVO message = messageMapper.initUserMessage(uid);
        if (message.getAvatar() != null)
            message.setAvatar(userAvatarPath + message.getAvatar());
        return message;
    }

    @Override
    public Integer getNoReadMessageCount(Integer userId) {
        return messageMapper.getNoReadCountByUid(userId);
    }
}
