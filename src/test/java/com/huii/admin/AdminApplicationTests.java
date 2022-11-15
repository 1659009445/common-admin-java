package com.huii.admin;

import com.huii.admin.common.utils.ExcelUtil;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.mapper.SysUserMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    void contextLoads() {
        String raw = "admin";
        String str = encoder.encode(raw);
        System.out.println(str);
        boolean b = encoder.matches(raw, encoder.encode(raw));
        System.out.println(b);
    }

    @Test
    void test(){
//        excelUtil.createExcel(sysUserMapper.selectList(null),SysUser.class);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        System.out.println(fmt.format(time));
    }

}
