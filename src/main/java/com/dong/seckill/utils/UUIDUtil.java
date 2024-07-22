package com.dong.seckill.utils;

import java.util.UUID;

/**
 * @Description: UUID工具类
 * @Author: Dong
 * @Date: 2024/7/2
 */
public class UUIDUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
