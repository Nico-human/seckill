package com.dong.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    //TODO: MD5不安全, 更换为SHA256加密
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFromPass(String inputPass){
        //需要与前端加密代码一致
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String fromPassToDBPass(String fromPass, String salt){
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String password = "1998816sosmp3";
        System.out.println(inputPassToFromPass(password));
        System.out.println(fromPassToDBPass(inputPassToFromPass(password), "1a2b3c4d"));
    }

}
