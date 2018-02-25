package com.miller.mining.controller;

import com.miller.mining.callback.ControllerCallbackHandler;
import com.miller.mining.comm.ResponseCodeEnum;
import com.miller.mining.exception.LoginFailedException;
import com.miller.mining.exception.TransforVoFaildedException;
import com.miller.mining.exception.VerifyException;
import com.miller.mining.model.User;
import com.miller.mining.service.JedisService;
import com.miller.mining.service.UserService;
import com.miller.mining.utils.EncryUtil;
import com.miller.mining.utils.JsonUtil;
import com.miller.mining.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JedisService jedisService;

    @RequestMapping(value = "register")
    @ResponseBody
    public Map<String,Object> register(final HttpServletRequest request,
                                       @RequestBody RequestVo requestVo) {
        Map<String,Object> map = new HashMap<String,Object>();

        try {
            this.executeWithoutLogin(request, requestVo, new ControllerCallbackHandler() {
                @Override
                public void doMVC(HttpServletRequest request, String requestContent) throws VerifyException, TransforVoFaildedException {
                    //把json内容转化为vo
                    UserVo userVo = JsonUtil.transforJsonToVO(requestContent,UserVo.class);
                    //转化失败的话抛出异常
                    if(null == userVo) {
                        throw new TransforVoFaildedException("转换UserRegisterVo失败");
                    }
                    //注册用户
                    User user = UserDoConverter.convertUserVoToUserDo(userVo);
                    userService.registerUser(user);

                    map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
                    map.put("resultMessage",ResponseCodeEnum.SUCCESS.getDescription());
                }
            });
        } catch (VerifyException e) {
            map.put("resultCode",ResponseCodeEnum.VERIFY_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        } catch (LoginFailedException e) {
            map.put("resultCode",ResponseCodeEnum.LOGIN_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        } catch (TransforVoFaildedException e) {
            map.put("resultCode",ResponseCodeEnum.DATA_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        }


        return map;
    }

    @RequestMapping(value = "login")
    @ResponseBody
    public Map<String,Object> login(final HttpServletRequest request,
                                       @RequestBody RequestVo requestVo) {
        //声明相应的map
        Map<String,Object> map = new HashMap<String,Object>();

        //执行具体MVC实现
        try {
            this.executeWithoutLogin(request, requestVo, new ControllerCallbackHandler() {
                @Override
                public void doMVC(HttpServletRequest request, String requestContent) throws VerifyException, TransforVoFaildedException, LoginFailedException {
                    //把解密后的报文转换为vo
                        UserVo userVo = JsonUtil.transforJsonToVO(requestContent,UserVo.class);
                        //转化失败的话抛出异常
                        if(null == userVo) {
                            throw new  TransforVoFaildedException("转换UserPermitionVo失败");
                        }

                        User user = UserDoConverter.convertUserVoToUserDo(userVo);
                        boolean loginSuccess = userService.login(user);

                        if(loginSuccess) {
                            //登陆成功后，需要向redis中添加信息,
                            //生成用户token
                            String token = EncryUtil.AESEncode(user.getUsername() + user.getPhoneId());
                            logger.info("根据用户名和手机标识码生成登陆TOKEN:" + token);
                            boolean isCached = jedisService.set(user.getUsername(),token);
                            if(!isCached) {
                                throw new LoginFailedException("无法把登陆信息存储到缓存");
                            }
                            map.put("userToken",token);
                        } else {
                            throw new VerifyException("用户名或密码不正确") ;
                        }

                    map.put("resultCode", ResponseCodeEnum.SUCCESS.getCode());
                    map.put("resultMessage",ResponseCodeEnum.SUCCESS.getDescription());

                }
            });
        } catch (VerifyException e) {
            map.put("resultCode",ResponseCodeEnum.VERIFY_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        } catch (LoginFailedException e) {
            map.put("resultCode",ResponseCodeEnum.LOGIN_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        } catch (TransforVoFaildedException e) {
            map.put("resultCode",ResponseCodeEnum.DATA_INVALID.getCode());
            map.put("resultMessage",e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "redis/set")
    @ResponseBody
    public void setRedis(final HttpServletRequest request,
                                    @RequestBody RedisVo redisVo) {
        jedisService.set(redisVo.getKey(),redisVo.getValue());
    }

    @RequestMapping(value = "redis/get/{key}")
    @ResponseBody
    public Map<String,Object> getRedis(final HttpServletRequest request,
                      @PathVariable("key") String key) {
        Map<String,Object> map = new HashMap<String,Object>();
        String value = jedisService.get(key);
        map.put(key,value);
        return map;
    }
}
