package com.miller.mining.utils;

import com.miller.mining.comm.encrypt.DES;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryUtil {

    private static final String encryptKey = "12345678";

    public static SecretKey getKey(String rules) throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(128,new SecureRandom(rules.getBytes()));
        SecretKey original = keygen.generateKey();
        byte[] raw = original.getEncoded();
        SecretKey key = new SecretKeySpec(raw,"AES");
        return key;
    }

    public static String AESEncode(String content) {
        try {
            SecretKey key = getKey(EncryUtil.encryptKey);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] enodeBytes = content.getBytes("utf-8");
            byte[] aesBytes = cipher.doFinal(enodeBytes);
            String encodedStr = Base64.getEncoder().encodeToString(aesBytes);
            return encodedStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String AESDecode(String content) {
        try {
            SecretKey key = getKey(EncryUtil.encryptKey);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] decodeBytes = Base64.getDecoder().decode(content);
            byte[] aesBytes = cipher.doFinal(decodeBytes);
            String encodedStr = new String(aesBytes,"utf-8");
            return encodedStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
    public static void main(String[] args) {
        String str = "{\"username\":\"啊是大\",\"password\":\"123456\"}";
        String t = EncryUtil.AESEncode(str);
        System.out.println("加密前:" + str);
        System.out.println("加密后:" + t);
        System.out.println("解密后:" + EncryUtil.AESDecode(t));
    }**/
}
