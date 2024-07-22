package com.dong.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Boolean> redisScript;

    @Test
    public void testLock01(){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //占位(加锁), 如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        if(isLock){
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            //操作结束, 释放锁
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用, 请稍后再试");
        }

    }

    @Test
    public void testLock02(){

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //给锁添加一个过期时间, 防止应用在运行过程中抛出异常导致锁无法正常释放
        //新问题: 假如上一个程序由于某些原因运行时间过长, 会导致上一个程序删除了下一个程序的锁
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            Integer.parseInt("xxxx"); // 假如运行过程中出现了异常, 导致锁无法正常释放
            redisTemplate.delete("k1");
        }else{
            System.out.println("有线程在使用, 请稍后再试");
        }
    }

    @Test
    public void testLock03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        // 类似乐观锁
        // lua脚本保证了原子性: (获取锁, 比较锁, 删除锁)
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        if(isLock){
            valueOperations.set("name", "xxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            Integer.parseInt("xxxx");
            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else{
            System.out.println("有线程在使用, 请稍后再试");
        }

    }


}
