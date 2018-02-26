package com.miller.mining.service;

import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.User;

public interface UserService {

    /**
     * 用户注册
     * @param user
     * @throws VerifyException
     */
    public void registerUser(User user) throws VerifyException;

    /**
     * 用户登陆
     * @param user
     * @return
     */
    public boolean login(User user) throws VerifyException;
    
    public User getUserByUsername(String username);
}
