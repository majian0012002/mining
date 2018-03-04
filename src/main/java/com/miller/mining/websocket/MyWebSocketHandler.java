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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class  );

    public static final Map<String,WebSocketSession> userSessionMap;

    public static AtomicInteger totalUser = new AtomicInteger(0);

    @Autowired
    private MiningService miningService;

    static {
        userSessionMap = new ConcurrentHashMap<String,WebSocketSession>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        logger.info("开始建立连接====");
        String username = (String) webSocketSession.getAttributes().get("username");
        userSessionMap.put(username,webSocketSession);
        totalUser.incrementAndGet();
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        if(webSocketMessage.getPayloadLength()==0)
            return;

        Map<String,String> map = new HashMap<String,String>();
        String username = (String) webSocketSession.getAttributes().get("username");
        MiningSingleVo miningVo = JsonUtil.transforJsonToVO(webSocketMessage.getPayload().toString(), MiningSingleVo.class);
        //转化失败抛异常
        if(null == miningVo) {
            throw new VerifyException("解密后的报文信息转化vo异常");
        }

        MiningInfo miningInfo = miningService.getMiningInfoById(miningVo.getMiningId());

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

        sendToUser(username, new TextMessage(JSON.toJSONString(map)));
    }

    /**
     * 向用户发送信息
     * @param username
     * @param message
     * @throws IOException
     */
    private void sendToUser(String username, TextMessage message) throws IOException {
        WebSocketSession session = userSessionMap.get(username);
        if(null != session && session.isOpen()) {
            session.sendMessage(message);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        logger.info("通信出现异常，断开连接");
        String username = (String) webSocketSession.getAttributes().get("username");
        userSessionMap.remove(username);
        totalUser.decrementAndGet();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        logger.info("开始失去连接====");
        String username = (String) webSocketSession.getAttributes().get("username");
        userSessionMap.remove(username);
        totalUser.decrementAndGet();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
