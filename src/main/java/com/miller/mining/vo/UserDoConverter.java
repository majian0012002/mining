package com.miller.mining.vo;

import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.User;
import com.miller.mining.utils.EncryUtil;

public class UserDoConverter {

    public static User convertUserVoToUserDo(UserVo userVo) throws VerifyException {
        User user = new User();
        user.setUsername(userVo.getUsername());
        user.setPhoneId(userVo.getPhoneid());
        user.setPassword(EncryUtil.base64Encode(userVo.getPassword()));

        return user;
    }

}
