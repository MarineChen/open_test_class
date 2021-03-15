package com.example.demo.config;

import com.example.demo.utils.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * MD5加密
 */
public class  MyPasswordEncoder implements PasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Util.encode((String) rawPassword));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.encode((String) rawPassword);
    }
}