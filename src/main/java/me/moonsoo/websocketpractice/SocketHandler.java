package me.moonsoo.websocketpractice;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {

    //key: 사용자 계정, value: 사용자 세션
    HashMap<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connection established");

        Map<String, Object> httpSession = session.getAttributes();
        User user = (User) httpSession.get("user");
        System.out.println(httpSession.get("user"));

        String account = user.getAccount();
        sessions.put(account, session);//해시 맵에 사용자 세션 정보 등록
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("message received");
        Map<String, Object> httpSession = session.getAttributes();
        User user = (User) httpSession.get("user");
        String account = user.getAccount();
        System.out.println("sent by " + account + ", message: " + message.getPayload());

        //사용자 계정, 메세지 json
        JSONObject chatData = new JSONObject();
        chatData.put("account", account);
        chatData.put("message", message.getPayload());

        sessions.entrySet().stream().filter(s -> !s.getKey().equals(account)).map(Map.Entry::getValue).forEach(s -> {
            try {
                s.sendMessage(new TextMessage(chatData.toJSONString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("connection closed");

        Map<String, Object> httpSession = session.getAttributes();

        User user = (User) httpSession.get("user");
        String account = user.getAccount();
        sessions.remove(account);//맵에서 사용자 제거
        session.close();//세션 종료

    }
}