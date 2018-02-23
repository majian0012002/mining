package com.miller.mining.controller;

import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.utils.EncryUtil;
import com.miller.mining.vo.RequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void execute(HttpServletRequest request, RequestVo reqVo, ModelMap map) {
        try {
            if(null == reqVo || reqVo.getContent().equals("")) {
                throw new VerifyException("请求报文为空");
            }
            String content = EncryUtil.AESDecode(reqVo.getContent());
            logger.info("经过转换后的请求报文:\n" + content);
            if(null == content || content.equals("")) {
                throw new VerifyException("转换报文失败");
            }
        } catch (VerifyException e) {
            e.printStackTrace();
            map.put("resultCode",ResponseCodeEnum.VERIFY_INVALID.getCode());
            map.put("resultMessage","无法获取用户登陆信息");
        }
    }
}
