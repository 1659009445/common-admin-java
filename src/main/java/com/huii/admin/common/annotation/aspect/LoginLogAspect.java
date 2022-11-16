package com.huii.admin.common.annotation.aspect;

import com.alibaba.fastjson.JSON;
import com.huii.admin.common.annotation.LoginLog;
import com.huii.admin.common.exception.KaptchaException;
import com.huii.admin.common.utils.IpAddressUtil;
import com.huii.admin.common.utils.UserAgentUtils;
import com.huii.admin.modules.common.entity.SysInfoLogin;
import com.huii.admin.modules.common.mapper.SysInfoLoginMapper;
import com.huii.admin.modules.common.service.SysInfoLoginService;
import com.huii.admin.security.entity.UserDetail;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Log4j2
@Component
public class LoginLogAspect {

	@Autowired
	private IpAddressUtil ipAddressUtil;

	@Autowired
	private UserAgentUtils userAgentUtils;

	@Autowired
	private SysInfoLoginService sysInfoLoginService;

	/**
	 * 切入点
	 */
	@Pointcut("@annotation(com.huii.admin.common.annotation.LoginLog)")
	public void logPointCut() {
	}

	/**
	 * 成功返回
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(value = "logPointCut()", returning = "result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		handleLog(joinPoint,null,result);
	}

	/**
	 * 失败返回
	 * @param joinPoint
	 * @param e
	 * @throws Exception
	 */
	@AfterThrowing(value = "logPointCut()", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Exception e) throws Exception {
		handleLog(joinPoint,e,null);
	}

	private void handleLog(final JoinPoint joinPoint, final Exception e, Object result){
		LoginLog annotation = getAnnotationLog(joinPoint);
		if(annotation == null){
			return;
		}
		SysInfoLogin sysInfoLogin = new SysInfoLogin();

		//获取用户信息
		try {
			UserDetail principal = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(ObjectUtils.isNotEmpty(principal)){
				String username = principal.getUser().getUsername();
				sysInfoLogin.setUserName(username);
			}
		} catch (RuntimeException ex) {
			log.error("异常信息:{}", ex.getMessage());
		}

		//获取request对象
		HttpServletRequest request = getHttpServletRequest();
		//获取浏览器信息和操作系统信息
		String ua = request.getHeader("User-Agent");
		String borderName = userAgentUtils.getBorderGroup(ua);
		String os = userAgentUtils.getOs(ua);
		sysInfoLogin.setLoginBrowser(borderName);
		sysInfoLogin.setLoginSystem(os);
		//获取IP地址和真实地址
		String ipAddress = ipAddressUtil.getIpAddress(request);
		sysInfoLogin.setLoginIp(ipAddress);
		String realAddress = ipAddressUtil.getRealAddress(ipAddress);
		sysInfoLogin.setLoginPlace(realAddress);
		//获取登录时间
		LocalDateTime localDateTime = LocalDateTime.now();
		sysInfoLogin.setLoginTime(localDateTime);

		if(!annotation.isLoginSuccess()){
			//登陆失败
			sysInfoLogin.setLoginInfo("登陆失败");
			//获取返回参数
			String jsonString = JSON.toJSONString(result);
			String requestParam = null;
			String requestMethod = request.getMethod();
			if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
				requestParam = argsArrayToString(joinPoint.getArgs());
				String substring = requestParam.substring(0, 200);
				if(StringUtils.contains(substring,"账号或密码错误")){
					sysInfoLogin.setErrInfo("账号或密码错误");
				} else if(StringUtils.contains(substring,"验证码错误")){
					sysInfoLogin.setErrInfo("验证码错误");
				}else {
					sysInfoLogin.setErrInfo("500");
				}
			}

		} else {
			sysInfoLogin.setLoginInfo("登录成功");
		}
		sysInfoLoginService.save(sysInfoLogin);
	}

	/**
	 * 获取注解
	 * @param joinPoint
	 * @return
	 */
	private LoginLog getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		return method!= null ? method.getAnnotation(LoginLog.class) : null;
	}

	/**
	 * 获取请求
	 * @return
	 */
	private HttpServletRequest getHttpServletRequest() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)attributes;
		assert servletRequestAttributes != null;
		return servletRequestAttributes.getRequest();
	}

	private String argsArrayToString(Object[] paramsArray) {
		StringBuilder params = new StringBuilder();
		if (paramsArray != null && paramsArray.length > 0) {
			for (Object o : paramsArray) {
				if (!isFilterObject(o)) {
					try {
						Object jsonObj = JSON.toJSON(o);
						params.append(jsonObj.toString()).append(" ");
					} catch (Exception ignored) {
					}
				}
			}
		}
		return params.toString().trim();
	}

	public boolean isFilterObject(final Object o) {
		return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
	}
}
