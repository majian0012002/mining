package com.miller.mining.controller;

import com.miller.mining.vo.MiningSingleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/miningws")
    public void handleMining(MiningSingleVo message) {

    }
}
