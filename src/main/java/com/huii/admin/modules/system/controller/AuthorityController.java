package com.huii.admin.modules.system.controller;

import com.google.code.kaptcha.Producer;
import com.huii.admin.common.lang.Const;
import com.huii.admin.common.result.Result;
import com.huii.admin.common.utils.ExcelUtil;
import com.huii.admin.common.utils.RandomUtil;
import com.huii.admin.common.utils.RedisTemplateUtil;
import com.huii.admin.modules.common.service.MailService;
import com.huii.admin.modules.system.entity.SysUser;
import com.huii.admin.modules.system.service.SysUserService;
import com.huii.admin.security.util.JwtAuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "auth")
@RestController
@RequestMapping("auth")
public class AuthorityController {

    @Autowired
    private Producer producer;

    @Autowired
    private JwtAuthUtil jwtAuthUtil;

    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RandomUtil randomUtil;

    /**
     * Kaptcha 验证码
     * @return Kaptcha
     * @throws IOException e
     */
    @ApiOperation("生成登录验证码")
    @GetMapping("/kaptcha")
    public Result<Map<String,String>> getKaptcha() throws IOException {

        String key = UUID.randomUUID().toString();

        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";
        String kaptcha = str + encoder.encode(outputStream.toByteArray());

        redisTemplateUtil.setCacheObject(Const.KAPTCHA_PREFIX+key,code,jwtAuthUtil.getSINGLE_TIME(), TimeUnit.SECONDS);

        Map<String,String> map = new HashMap<>();
        map.put("key",key);
        map.put("kaptcha",kaptcha);

        //TODO 测试环境用
        System.out.println(code);
        System.out.println(key);

        return Result.success(map);
    }

    @ApiOperation("生成验证码")
    @GetMapping("/verify")
    public Result<?> sendResetPasswordVerifyCode(){
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        SysUser user = sysUserService.getById(id);
        String random = randomUtil.createRandom(6);
        mailService.sendResetPassCode(user.getEmail(),random);
        redisTemplateUtil.setCacheObject(Const.RESET_PASS_CODE_PREFIX+id,random,30,TimeUnit.MINUTES);
        return Result.success(null);
    }

    @ApiOperation("校验验证码")
    @GetMapping("/verify/{code}")
    public Result<Boolean> isVerifyCodeEqual(@PathVariable("code") String code){
        Long id = Long.parseLong((String) request.getAttribute(Const.USER_ID));
        String __code = (String) redisTemplateUtil.getCacheObject(Const.RESET_PASS_CODE_PREFIX + id);
        return Result.success(__code.equals(code));
    }


    //TODO 修改密码获取验证码 mailUtil

    @ApiOperation("生成下载文件名")
    @GetMapping("/name")
    public Result<String> getDownloadFileName() {
        return Result.success(excelUtil.getName());
    }
}
