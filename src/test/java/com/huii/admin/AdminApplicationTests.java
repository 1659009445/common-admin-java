package com.huii.admin;

import com.huii.admin.common.utils.IpAddressUtil;
import com.huii.admin.common.utils.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    TimeUtil timeUtil;


    @Autowired
    IpAddressUtil ipAddressUtil;
    @Test
    void contextLoads() {

        String address = ipAddressUtil.getRealAddress("192.168.0.1");
        System.out.println(address);
    }
}
