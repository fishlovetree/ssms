package com.ws.ssms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.MenuService;
import com.ws.ssms.system.service.UserService;
import com.ws.ssms.system.util.MD5Util;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 登录接口类
 */
@RestController
@Api(tags="前台登录类")
public class LoginController {

	@Resource
    private UserService userService;
	
	@Resource
    private MenuService menuService;

    @ApiOperation(value = "前台登录", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "username", value = "账号", dataType = "String", paramType = "query"),
    	@ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="登录成功"),
	    @ApiResponse(code = 0, message="登录失败"),
	    @ApiResponse(code = 3, message="参数错误"),
	    @ApiResponse(code = 401, message="账号不存在"),
	    @ApiResponse(code = 402, message="密码错误")
	})
    @PostMapping("/login")
    public ResponseData login(@RequestParam("username") String username, @RequestParam("password") String password,
    	HttpServletRequest request, HttpServletResponse response) {
    	if (null == username || null == password){
    		return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", "");
    	}
        User user = userService.getUserByName(username);
        if (null == user || user.getUsertype() == 0){
        	return new ResponseData(ResponseEnum.BADUSERNAME.getCode(), "账号不存在", "");
        }
        if (!user.getPassword().equals(MD5Util.MD5(password))){
        	return new ResponseData(ResponseEnum.ERRORPASSWORD.getCode(), "密码错误", "");
        }
		//回写用户菜单权限
		List<Menu> menuList = menuService.getUserMenuList(user.getId(), user.getUsertype(), user.getUserLevel());
		user.setMenuList(menuList);
		request.getSession().setAttribute("ws_user", JSON.toJSONString(user));
		//获取用户菜单树形结构
		List<Map<String, Object>> menuTree = menuService.getUserMenuTree(user.getId(), user.getUsertype(), user.getUserLevel());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sessionid", request.getSession().getId());
		map.put("menujson", menuTree);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "登录成功", map);
    }
    
    @ApiOperation(value = "前台登出", httpMethod = "POST")
	@ApiResponses({
	    @ApiResponse(code = 1, message="退出登录成功"),
	    @ApiResponse(code = 0, message="退出登录失败")
	})
    @PostMapping("/logout")
    public ResponseData logout(HttpServletRequest request, HttpServletResponse response)throws Exception {
    	User user = JSON.parseObject((String)request.getSession().getAttribute("ws_admin_user"), User.class);
		if (null != user) {
			request.getSession().removeAttribute("ws_user");
			request.getSession().invalidate();
		}
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "退出登录成功", null);
    }
}
