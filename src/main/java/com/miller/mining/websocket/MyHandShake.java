package com.miller.mining.websocket;

import com.miller.mining.service.JedisService;
import com.miller.mining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MyHandShake implements HandshakeInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private JedisService jedisService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        boolean result = false;

        if(serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest request = ((ServletServerHttpRequest)serverHttpRequest).getServletRequest();
            String username = request.getParameter("username");
            String token = request.getParameter("token");
            String cacheToken = jedisService.get(username);
            if(token.equals(cacheToken)) {
                map.put("username",username);
                result = true;
            }
        }
        return result;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
