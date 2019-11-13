package com.ws.ssms.business.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ws.ssms.business.util.ResponseData;
import com.ws.ssms.business.util.ResponseEnum;

@RestControllerAdvice
public class ExceptionHandlers {
	Logger logger = LoggerFactory.getLogger(ExceptionHandlers.class);

	@ExceptionHandler(value = Exception.class)
	public ResponseData handle(Exception e) {
		logger.info(e.toString());
		return new ResponseData(ResponseEnum.ERROR.getCode(), e.getMessage(), null);
	}

	/**
	 * 获取当前网络ip(可以获取通过了Apache，Squid等反向代理软件的客户端的真实IP地址)
	 * 
	 * @param request
	 * @return
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
															// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 获取来访者的浏览器版本
	 * 
	 * @param request
	 * @return
	 */
	public String getRequestBrowserInfo(HttpServletRequest request) {
		String browserVersion = null;
		String agent = request.getHeader("User-Agent").toLowerCase();
		if (agent == null || agent.equals("")) {
			return "";
		}
		// System.out.print(agent);
		if (agent.indexOf("msie 7") > 0) {
			browserVersion = "IE7";
		} else if (agent.indexOf("msie 8") > 0) {
			browserVersion = "IE8";
		} else if (agent.indexOf("msie 9") > 0) {
			browserVersion = "IE9";
		} else if (agent.indexOf("msie 10") > 0) {
			browserVersion = "IE10";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			browserVersion = "IE11";
		} else if (agent.indexOf("opera") > 0) {
			browserVersion = "Opera";
		} else if (agent.indexOf("firefox") > 0) {
			browserVersion = "Firefox";
		} else if (agent.indexOf("chrome") > 0) {
			browserVersion = "Chrome";
		} else if (agent.indexOf("webkit") > 0) {
			browserVersion = "Webkit";
		} else if (agent.indexOf("safari") > 0) {
			browserVersion = "Safari";
		} else if (agent.indexOf("camino") > 0) {
			browserVersion = "Camino";
		} else if (agent.indexOf("konqueror") > 0) {
			browserVersion = "Konqueror";
		} else {
			browserVersion = "Others";
		}
		return browserVersion;
	}

	public String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		try {
			throwable.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
}
