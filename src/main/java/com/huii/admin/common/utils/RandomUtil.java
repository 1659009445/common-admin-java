package com.huii.admin.common.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机数生成工具
 * @author hy
 */
@Component
public class RandomUtil {

    public String createRandom(int length){
        //生成随机种子
        Random random = new Random();
        int number = random.nextInt(10000);
        //生成随机数
        Random random_ = new Random(number);
        String code = "";
        for(int i =0; i<length; i++){
            code += random_.nextInt(10);
        }

        return code;
    }

}
