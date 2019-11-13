package com.ws.ssms.business.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.business.model.Menu;
import com.ws.ssms.business.model.User;

/**
 * @author hxl
 * @description 权限拦截器
 * @date 2019/11/6
 * @since 1.0
 */
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//先判断有没有登录
    	HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		if (null == user){
			//response.sendError(HttpStatus.FORBIDDEN.value(), "未登录");
			response.sendRedirect(request.getContextPath() + "/admin/notlogon");
			return false;
		}
        // 验证权限
        if (this.hasPermission(handler, user.getMenuList())) {
            return true;
        }
        //  null == request.getHeader("x-requested-with") TODO 暂时用这个来判断是否为ajax请求
      //response.sendError(HttpStatus.FORBIDDEN.value(), "未登录");
		response.sendRedirect(request.getContextPath() + "/admin/unauth");
        return false;
    }

    /**
     * 是否有权限
     *
     * @param handler
     * @return
     */
    private boolean hasPermission(Object handler, List<Menu> list) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上的注解
            RequiredPermission requiredPermission = handlerMethod.getMethod().getAnnotation(RequiredPermission.class);
            // 如果方法上的注解为空 则获取类的注解
            if (requiredPermission == null) {
                requiredPermission = handlerMethod.getMethod().getDeclaringClass().getAnnotation(RequiredPermission.class);
            }
            // 如果标记了注解，则判断权限
            if (requiredPermission != null && !requiredPermission.value().isEmpty() && null != list) {
                List<String> permissionSet = new ArrayList<String>();
            	for(Menu menu:list){
        	        String menuUrl = menu.getMenuurl();
           		  	if(null != menuUrl && !"".equals(menuUrl.trim())){
           			    String[] s = menuUrl.split("/");
           			    if(null != s && s.length >= 1){
           				    StringBuilder mSrt = new StringBuilder();
           				    for(int i = 1; i < s.length; i++){
           				        mSrt.append(s[i] + ":");
           				    }
           				    mSrt.setLength(mSrt.length() - 1);//去掉最后一个“:”
           				    permissionSet.add(mSrt.toString());
           			    }
           		    }
           	    }
                return permissionSet.contains(requiredPermission.value());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // TODO
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO
    }
}
