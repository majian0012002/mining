package com.miller.mining.service.impl;

import com.miller.mining.exception.VerifyException;
import com.miller.mining.mapper.UserMapper;
import com.miller.mining.model.User;
import com.miller.mining.service.JedisService;
import com.miller.mining.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;


    @Override
    public void registerUser(User user) throws VerifyException {
        //进行校验
        verifyUserInfo(user);
        //首先查询是否有重复用户名
        logger.info("开始校验是否有重复用户名:");
        int count = userMapper.countByUsername(user.getUsername());

        if(count > 0) {
            logger.info("数据库中存在重复的用户名：" + user.getUsername());
            throw new VerifyException("数据库中存在重复的用户名");
        }

        userMapper.insert(user);
    }

    private void verifyUserInfo(User user) throws VerifyException {
        if(null == user.getUsername()|| user.getUsername().equals("")) {
            throw new VerifyException("用户名不能为空");
        }
        if(null == user.getPassword()|| user.getPassword().equals("")) {
            throw new VerifyException("密码不能为空");
        }
        if(null == user.getPhoneId()|| user.getPhoneId().equals("")) {
            throw new VerifyException("手机唯一标识不能为空");
        }
    }

    @Override
    public boolean login(User user) throws VerifyException {
        //对传入的user参数进行校验
        verifyUserInfo(user);
        //根据用户名查询用户
        User foundUser = userMapper.selectUserByUsername(user.getUsername());
        if(null != foundUser && user.getPassword().equals(foundUser.getPassword())) {
            //若phoneid不一致，说明更换手机登陆，需要修改数据库中phoneid
            if(!user.getPhoneId().equals(foundUser.getPhoneId())) {
                foundUser.setPhoneId(user.getPhoneId());
                userMapper.updateByPrimaryKey(foundUser);
            }
            return true;
        }
        return false;
    }

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		User user = userMapper.selectUserByUsername(username);
		return user;
	}
}
