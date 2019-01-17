package com.example.permission.util;

import org.springframework.mail.SimpleMailMessage;

/**
 * @author CookiesEason
 * 2019/01/17 15:58
 */
public class EmailUtil {

    public static SimpleMailMessage send(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("837447352@qq.com");
        message.setTo(email);
        message.setSubject("密码");
        message.setText("密码: " + password);
        return message;
    }
}
