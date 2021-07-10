package org.example.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class MessageCenter implements InitializingBean {


    @Autowired
    private ObjectMapper objectMapper;

    private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();

    private ConcurrentHashMap<Integer, Session> clientsMap = new ConcurrentHashMap<>();

    public void addOnlineUser(Integer userId, Session session){
        clientsMap.put(userId, session);
    }

    public void delOnlineUser(Integer userId){
        clientsMap.remove(userId);
    }

    public void addMessage(Message message){
        messageQueue.add(message);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, 100, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.DiscardPolicy()
        );
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        //获取数据，如果队列为空，阻塞等待
                        Message message = messageQueue.take();
                        for (Map.Entry<Integer, Session> entry : clientsMap.entrySet()) {
                            Session session = entry.getValue();
                            session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
