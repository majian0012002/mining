package com.miller.mining.websocket;

import com.alibaba.fastjson.JSON;
import com.miller.mining.comm.MiningTypeEnum;
import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.MiningInfo;
import com.miller.mining.service.MiningService;
import com.miller.mining.utils.JsonUtil;
import com.miller.mining.vo.MiningInfoVo;
import com.miller.mining.vo.MiningSingleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/miningws")
@Component
public class MyWebSocketHandler{

    private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class  );

    public static final Map<String,Session> userSessionMap;

    public static AtomicInteger totalUser = new AtomicInteger(0);

    @Autowired
    private MiningService miningService;

    static {
        userSessionMap = new ConcurrentHashMap<String,Session>();
    }

    @OnOpen
    public void afterConnectionEstablished(Session webSocketSession) throws Exception {
        logger.info("开始建立连接====");
        String username = webSocketSession.getId();
        userSessionMap.put(username,webSocketSession);
        totalUser.incrementAndGet();
    }

    @OnMessage
    public void handleMessage(Session webSocketSession, String webSocketMessage) throws Exception {
//        if(webSocketMessage.getPayloadLength()==0)
//            return;

        Map<String,String> map = new HashMap<String,String>();
        String username = (String) webSocketSession.getId();
        MiningSingleVo miningVo = JsonUtil.transforJsonToVO(webSocketMessage, MiningSingleVo.class);
        //转化失败抛异常
        if(null == miningVo) {
            throw new VerifyException("解密后的报文信息转化vo异常");
        }

        MiningInfo miningInfo = miningService.getMiningInfoById(Integer.parseInt(miningVo.getMiningId()));

        //查询不出来，抛异常
        if(null == miningInfo) {
            throw new MiningException("无法根据id查询出挖矿活动");
        }

        //查询出的活动不在进行中，抛异常
        if(miningInfo.getState() != 0) {
            throw new MiningException("当前挖矿已经结束，请开始下次挖矿");
        }

        //挖矿类型不一致抛出异常
        if(miningInfo.getType() != miningVo.getMingType()) {
            throw new MiningException("挖矿类型错误");
        }

        String miningAmoutOfThisTime = "";
        //根据不同挖矿类型进行计算
        switch(MiningTypeEnum.getByValue(miningVo.getMingType())) {
            case ORDINARY_MODE:
                miningAmoutOfThisTime = miningService.computeMiningAmoutInOrdinaryMode(miningInfo, miningVo);
                break;
            case SPORTS_MODE:
                miningAmoutOfThisTime = miningService.computeMiningAmoutInSportsMode(miningInfo, miningVo);
                break;
            default: break;
        }

        map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
        map.put("resultMessage","请求成功");
        map.put("increateCount", miningAmoutOfThisTime);
        map.put("totalUser",String.valueOf(MyWebSocketHandler.totalUser));

        sendToUser(username, JSON.toJSONString(map));
    }

    /**
     * 向用户发送信息
     * @param username
     * @param message
     * @throws IOException
     */
    private void sendToUser(String username, String message) throws IOException {
        Session session = userSessionMap.get(username);
        if(null != session && session.isOpen()) {
            session.getBasicRemote().sendText(message);
        }
    }

    @OnError
    public void handleTransportError(Session webSocketSession, Throwable throwable) throws Exception {
        logger.info("通信出现异常，断开连接");
        String username = webSocketSession.getId();
        userSessionMap.remove(username);
        totalUser.decrementAndGet();
    }

    @OnClose
    public void afterConnectionClosed() throws Exception {
        logger.info("开始失去连接====");
    }
}
