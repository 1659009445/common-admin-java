package com.huii.admin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void contextLoads() {
        String raw = "admin";
        String str = encoder.encode(raw);
        System.out.println(str);
        boolean b = encoder.matches(raw, encoder.encode(raw));
        System.out.println(b);
    }

}
