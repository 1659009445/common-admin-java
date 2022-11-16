package com.huii.admin.common.annotation.aspect;

import com.alibaba.fastjson.JSON;
import com.huii.admin.common.annotation.Log;
import com.huii.admin.common.utils.IpAddressUtil;
import com.huii.admin.modules.common.entity.SysInfoOperation;
import com.huii.admin.modules.common.service.SysInfoOperationService;
import com.huii.admin.security.entity.UserDetail;
import lombok.extern.log4j.Log4j2;
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
public class OpLogAspect {

	private final int MAX_LOG_LENGTH = 2000;

	@Autowired
	private IpAddressUtil ipAddressUtil;

	@Autowired
	private SysInfoOperationService sysInfoOperationService;

	/**
	 * 切入点
	 */
	@Pointcut("@annotation(com.huii.admin.common.annotation.Log)")
	public void logPointCut() {
	}

	/**
	 * 成功返回
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(value = "logPointCut()", returning = "result")
	public void afterReturning(JoinPoint joinPoint,Object result) {
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
		try {
			//获取注解
			Log log = getAnnotationLog(joinPoint);
			if(log == null){
				return;
			}
			//获取用户信息
			UserDetail principal = (UserDetail)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = principal.getUser().getUsername();
			//获取request对象
			HttpServletRequest request = getHttpServletRequest();
			//获取IP地址
			String ipAddress = ipAddressUtil.getIpAddress(request);
			//获取操做时间
			LocalDateTime localDateTime = LocalDateTime.now();
			//获取返回参数
			String jsonString = JSON.toJSONString(result);
			String __jsonString = StringUtils.substring(jsonString,0,MAX_LOG_LENGTH);
			//获取方法
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			//获取请求方式
			String requestMethod = request.getMethod();
			//获取请求参数
			String requestParam = null;
			if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
				requestParam = argsArrayToString(joinPoint.getArgs());
			}

			SysInfoOperation sysInfoOperation = new SysInfoOperation();
			sysInfoOperation.setUserName(username);
			sysInfoOperation.setOpIp(ipAddress);
			sysInfoOperation.setOpTime(localDateTime);
			sysInfoOperation.setOpController(className);
			sysInfoOperation.setOpMethod(methodName);
			sysInfoOperation.setReqMethod(requestMethod);
			sysInfoOperation.setOpTitle(log.title());
			if(log.isSaveRequestData() && StringUtils.isNotEmpty(requestParam) ){
				//是否存入请求参数
				sysInfoOperation.setReqParam(requestParam);
			}
			if(log.isSaveResponseData()){
				//是否存入返回参数
				sysInfoOperation.setResParam(__jsonString);
			}
			if(e != null){
				sysInfoOperation.setOpInfo("失败");
				sysInfoOperation.setErrInfo(e.getMessage());
			}
			sysInfoOperation.setOpInfo("成功");

			sysInfoOperationService.save(sysInfoOperation);
		} catch (Exception ex) {
			log.error("异常信息:{}", ex.getMessage());
		}
	}

	/**
	 * 获取注解
	 * @param joinPoint
	 * @return
	 */
	private Log getAnnotationLog(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		return method!= null ? method.getAnnotation(Log.class) : null;
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

	/**
	 * 封装请求参数
	 * @param paramsArray
	 * @return
	 */
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

	/**
	 * 过滤
	 * @param o
	 * @return
	 */
	public boolean isFilterObject(final Object o) {
		return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
	}

}
