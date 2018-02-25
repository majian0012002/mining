package com.miller.mining.controller;

import com.miller.mining.callback.ControllerCallbackHandler;
import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.LoginFailedException;
import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.TransforVoFaildedException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.utils.EncryUtil;
import com.miller.mining.vo.RequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    private Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void executeWithoutLogin(HttpServletRequest request, RequestVo reqVo, ControllerCallbackHandler callback) throws VerifyException, LoginFailedException, TransforVoFaildedException {
        /**
        if(null == reqVo || reqVo.getContent().equals("")) {
                throw new VerifyException("请求报文为空");
            }
            String content = EncryUtil.AESDecode(reqVo.getContent());
            logger.info("经过转换后的请求报文:\n" + content);
            if(null == content || content.equals("")) {
                throw new VerifyException("转换报文失败");
            }
        **/
        String content = execute(request,reqVo);
        callback.doMVC(request,content);
    }

    public void executeWithLogin(HttpServletRequest request, RequestVo reqVo, ControllerCallbackHandler callback) throws VerifyException, LoginFailedException, TransforVoFaildedException {
        String content = execute(request,reqVo);

        callback.doMVC(request,content);
    }

    public String execute(HttpServletRequest request, RequestVo reqVo) throws VerifyException {
        if(null == reqVo || reqVo.getContent().equals("")) {
            throw new VerifyException("请求报文为空");
        }
        String content = EncryUtil.AESDecode(reqVo.getContent());
        logger.info("经过转换后的请求报文:\n" + content);
        if(null == content || content.equals("")) {
            throw new VerifyException("转换报文失败");
        }

        return content;
    }
}
