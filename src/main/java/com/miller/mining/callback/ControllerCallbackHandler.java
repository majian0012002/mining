package com.miller.mining.callback;

import com.miller.mining.exception.LoginFailedException;
import com.miller.mining.exception.MiningException;
import com.miller.mining.exception.TransforVoFaildedException;
import com.miller.mining.exception.VerifyException;

import javax.servlet.http.HttpServletRequest;

public interface ControllerCallbackHandler {

    public void doMVC(HttpServletRequest request, String requestContent) throws VerifyException, TransforVoFaildedException, LoginFailedException;
    
    public boolean check(HttpServletRequest request, String requestContent) throws VerifyException;
}
