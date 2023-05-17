package io.renren.modules.takeout.websocket;

import io.renren.common.constant.Constant;
import io.renren.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: davidyoung 321740709@qq.com
 * @since: 2023/3/27
 *
 **/
@Component
@ServerEndpoint("/takeout/websocket")
@Slf4j
public class WebSocketServer {
    private Session session;
    @Autowired
    private RedisTemplate redisTemplate;

    private static Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // 获取客户端的token
        String token = getToken(session);
        if (token != null) {
            // 将token和Session对象保存到sessionMap中
            sessionMap.put(token, session);
        }
        this.session = session;


        log.info("【websocket消息】有新的连接, 总数:{}", sessionMap.size());

    }

    @OnClose
    public void onClose() {
        // 获取客户端的token
        String token = getToken(session);
        if (token != null) {
            // 从sessionMap中移除该客户端的映射关系
            sessionMap.remove(token);
        }
        log.info("【websocket消息】连接断开, 总数:{}", sessionMap.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
        if ("hello server".equals(message) || "heartbeat".equals(message)) {
            try {
                session.getBasicRemote().sendText("success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if ("您有一个新的外卖订单，请及时处理！".equals(message)) {

        }
    }

    public static void sendMessage(String token, String message) {
        Session session = sessionMap.get(token);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void sendMessage(String message) {

        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            Session session = entry.getValue();
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 获取客户端的token
    private String getToken(Session session) {
        Map<String, List<String>> paramMap = session.getRequestParameterMap();
        if (paramMap.containsKey("token")) {
            List<String> tokenList = paramMap.get("token");
            if (tokenList.size() > 0) {
                return tokenList.get(0);
            }
        }
        return null;
    }

    //监听Mq消息
    @RabbitListener(queues = Constant.ORDER_QUEUE)
    public void receive(String message) {
        log.info("【RabbitMq消息】收到队列的消息:{}", message);

        sendMessage(message);
    }
}
