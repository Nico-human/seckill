package com.dong.seckill.utils;

import com.dong.seckill.pojo.User;
import com.dong.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 生成用户工具类
 * @Author: Dong
 * @Date: 2024/7/15
 */

public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<User>(count);
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setId(13477626002L+i);
            user.setNickname("user"+i);
            user.setSalt("1a2b3c4d");
            user.setLoginCount(0);
            user.setRegisterDate(new Date());
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            users.add(user);
        }
        System.out.println("create user");

        //插入到数据库中
        Connection conn = getConn();
        String sql = "insert into t_user(login_count,nickname,register_date,salt,password,id) values(?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            statement.setInt(1, user.getLoginCount());
            statement.setString(2, user.getNickname());
            statement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            statement.setString(4, user.getSalt());
            statement.setString(5, user.getPassword());
            statement.setLong(6, user.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        conn.close();
        System.out.println("insert to db");

        //登录, 生成UserTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\Jo-Laptop\\Desktop\\config.txt"); // UserTicket存放位置
        if(file.exists()){
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (User user : users) {
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);

            OutputStream out = co.getOutputStream();

            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");

            out.write(params.getBytes());
            out.flush();

            InputStream input = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = input.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            input.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean<String> respBean = mapper.readValue(response, RespBean.class);
            String useTicket = (String) respBean.getObj();
            System.out.println("create userTicket: " + user.getId());
            String row = user.getId() + "," + useTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file: " + user.getId());
        }
        raf.close();
        System.out.println("over");
    }


    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "zhenxun";
        String password = "zhenxun123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
                
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
