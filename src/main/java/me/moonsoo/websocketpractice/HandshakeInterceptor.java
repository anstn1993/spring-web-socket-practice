package me.moonsoo.websocketpractice;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        HttpSession session = ((ServletServerHttpRequest) request).getServletRequest().getSession();

        User user = (User)session.getAttribute("user");
        System.out.println("session user in interceptor: " + user.getAccount());

        attributes.put("user", user);//WebSocketSession에 user데이터 등록

        return true;
    }
}
