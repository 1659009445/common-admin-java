package com.huii.admin.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class IpAddressUtil {

	private static final String URL = "https://ip.useragentinfo.com/json?ip=";

	/**
	 * 获取IP地址
	 * @param request
	 * @return
	 */
	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取真实地址 返回结果实例
	 * {
	 *     "country": "中国",
	 *     "short_name": "CN",
	 *     "province": "广东省",
	 *     "city": "",
	 *     "area": "",
	 *     "isp": "移动",
	 *     "net": "手机网络",
	 *     "ip": "117.136.12.79",
	 *     "code": 200,
	 *     "desc": "success"
	 * }
	 * @param ip
	 * @return
	 */
	public String getRealAddress(String ip) {
		if(ip.equals("0:0:0:0:0:0:0:1")){
			return "内网";
		}
		String result = HttpUtils.sendGet(URL + ip);
		JSONObject json = JSON.parseObject(result);
		String country = json.getString("country");
		String province = json.getString("province");
		return country+province;
	}


}