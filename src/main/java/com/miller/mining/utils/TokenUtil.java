package com.miller.mining.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TokenUtil {

    private static final String encryModel = "MD5";

    public static String md5(String str) {
        return encrypt(encryModel,str);
    }

    public static String encrypt(String algorithm, String str) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(str.getBytes());
            StringBuffer sb = new StringBuffer();
            byte[] bytes = md.digest();
            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xFF;
                if(b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String authentication(Map<String,String> srcData) {
        if (null == srcData)
            throw new RuntimeException("传入的生成token的参数为空");
        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String, String>>(srcData.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuffer srcSb = new StringBuffer();
        for (Map.Entry<String,String> srcAtom : list) {
            srcSb.append(String.valueOf(srcAtom.getValue()));
            srcSb.append("-");
        }

        String handlerStr = srcSb.toString().substring(0,srcSb.length() - 1);
        System.out.println("用户信息加密前的字符串：" + handlerStr);
        String token = md5(handlerStr);

        return token;
    }

    /**
    public static void main(String[] args) {
        Map<String,String> userMap = new HashMap<String, String>();
        userMap.put("username","miller");
        userMap.put("cellphoneId","ahwafalfaeigewgrgq12t32f");
        String token = authentication(userMap);

        System.out.println("生成的token是:" + token);
    }**/
}
