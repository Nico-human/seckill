package com.dong.seckill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码校验
 */
public class ValidatorUtil {

    //手机号码的正则表达式
    private static final Pattern mobile_pattern = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    public static boolean isMobile(String mobile) {
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_pattern.matcher(mobile);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String mobile = "13477626002";
        String mobile2 = "19854084736";
        System.out.println(isMobile(mobile));
    }

}
