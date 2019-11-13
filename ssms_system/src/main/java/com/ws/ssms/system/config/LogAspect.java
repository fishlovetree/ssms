package com.ws.ssms.system.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.system.model.SysLog;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.LogService;

/**
 * @Description:spring AOP切面类，主要将系统的操作记录日志写入数据库
 * @Author hxl
 * @Time: 2019年11月1日
 */
@Configuration
public class LogAspect {
	@Resource
	private LogService logService;

	public void doAfterReturning(JoinPoint jp) {
		
	}
	
	public void doAfterThrow(JoinPoint jp, Throwable ex) {
		
	}
	
	//自定义操作日志
    public void addLog(String sessionname, String title, String content, Integer type) {
		try {
			// 创建日志对象
			SysLog mLog = new SysLog();
			// 从session获取用户
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			HttpSession session = request.getSession();
			String json = (String)session.getAttribute("ws_admin_user");
			User user = JSON.parseObject(json, User.class); 
			// 获取IP
			String ip = getIpAddr(request);

			mLog.setUserid(user.getId().intValue());
			mLog.setIp(ip);
			mLog.setTitle(title);
			mLog.setContent(content);
			mLog.setOpertype(type);
			mLog.setIntime(new Date());
			
			// 将日志存入数据库
			logService.insert(mLog);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

}
