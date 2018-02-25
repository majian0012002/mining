package com.miller.mining.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miller.mining.vo.UserRegisterVo;

import java.io.IOException;

public class JsonUtil {

    private static ObjectMapper om;

    public static ObjectMapper getObjectMapper() {
        if(om == null) {
            om = new ObjectMapper();
        }
        return om;
    }

    public static <T> T transforJsonToVO(String jsonContent,Class<T> t) {
        try {
            return getObjectMapper().readValue(jsonContent, t);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String str = "{\"username\":\"miller\",\"password\":\"123456\",\"phoneid\":\"asdanawihfiaw\"}";
        UserRegisterVo vo = transforJsonToVO(str,UserRegisterVo.class);
    }
}
