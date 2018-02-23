package com.miller.mining.callback;

import com.miller.mining.vo.BaseMiningVo;
import com.miller.mining.vo.UserPermitionVo;

import javax.servlet.http.HttpServletRequest;

public interface ControllerCallbackHandler {

    public void doMVC(HttpServletRequest request, String requestContent);
}
