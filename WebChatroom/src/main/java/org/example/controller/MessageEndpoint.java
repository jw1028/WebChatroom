package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.MessageMapper;
import org.example.mapper.UserMapper;
import org.example.model.Message;
import org.example.model.MessageCenter;
import org.example.model.User;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@ServerEndpoint("/message/{userId}")
@Component
public class MessageEndpoint implements ApplicationContextAware {

    private static ObjectMapper Mapper;

    private static MessageCenter MessageCenter;

    private static UserMapper userMapper;

    private static MessageMapper messageMapper;

    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId, Session session) throws IOException {
        log.info("websocket open: {}", userId);
        MessageCenter.addOnlineUser(userId, session);
        User user = userMapper.selectByPrimaryKey(userId);
        List<Message> messages = messageMapper.selectByLastLogout(user.getLastLogout());
        for(Message m : messages){
            session.getBasicRemote().sendText(Mapper.writeValueAsString(m));
        }
    }

    @OnClose
    public void onClose(@PathParam("userId") Integer userId){
        log.info("websocket close: {}", userId);
        MessageCenter.delOnlineUser(userId);
        User user = new User();
        user.setUserId(userId);
        user.setLastLogout(new Date());
        userMapper.updateByPrimaryKeySelective(user);
    }

    @OnMessage
    public void onMessage(String request, Session session) throws JsonProcessingException {
        log.info(request);
        Message message = Mapper.readValue(request, Message.class);
        MessageCenter.addMessage(message);
        messageMapper.insertSelective(message);
    }

    @OnError
    public void onError(Session session, Throwable t){
        log.error("websocket error", t);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Mapper = applicationContext.getBean(ObjectMapper.class);
        MessageCenter = applicationContext.getBean(MessageCenter.class);
        userMapper = applicationContext.getBean(UserMapper.class);
        messageMapper = applicationContext.getBean(MessageMapper.class);
    }
}
