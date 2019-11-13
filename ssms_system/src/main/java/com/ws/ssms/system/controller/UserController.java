package com.ws.ssms.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.system.config.RequiredPermission;
import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.UserService;
import com.ws.ssms.system.util.ApiKeyUtil;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/user")
@Api(tags="账号管理类")
public class UserController {

	@Autowired
	UserService userService;
	
	/**
	 * @Description 根据账号名获取账号
	 * @param userName 账号名
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "根据账号名获取账号信息", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userName", value = "账号名", required = true, dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@GetMapping("/getUserByName")
	@RequiredPermission("user:view")
    public ResponseData getUserByName(String userName) throws Exception{
		if (null == userName){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", userName);
		}
        User user = userService.getUserByName(userName);  
        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", user);
    }
	
	/**
	 * @Description 根据条件获取账号集合
	 * @param usertype 1-前台账号，0-后台账号
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "根据条件获取账号列表", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userType", value = "账号类型：1-前台账号，0-后台账号", required = false, dataType = "Integer"),
    	@ApiImplicitParam(name = "userName", value = "账号名", required = false, dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getUserList")
	@RequiredPermission("user:view")
	public ResponseData getUserList(HttpServletRequest request, HttpServletResponse response, Integer userType, String userName){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<User> userList = userService.getUserList(userType, user.getOrganizationcode(), userName);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", userList);
	}
	
	/**
	 * @Description 获取账号分页
	 * @param usertype 1-前台账号，0-后台账号
	 * @param username 账号名
	 * @param page 页数
	 * @param size 每页行数
	 * @return
	 * @throws Exception
	 * @Time 2019年11月7日
	 * @Author hxl
	 */
	@ApiOperation(value = "分页获取账号", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userType", value = "账号类型：1-前台账号，0-后台账号", dataType = "Integer"),
    	@ApiImplicitParam(name = "userName", value = "账号名", dataType = "String"),
    	@ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
    	@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getUserPage")
	@RequiredPermission("user:view")
	public ResponseData getUserPage(HttpServletRequest request, HttpServletResponse response, Integer userType, String userName, int page, int size){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<User> userList = userService.getUserPage(userType, user.getOrganizationcode(), userName, (page - 1) * size, size);
		int total = userService.getUserCount(userType, user.getOrganizationcode(), userName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", userList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 添加账号
	 * @param user
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加账号", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "user", value = "账号实体项", dataType = "User")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 2, message="账号已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("user:view")
	public ResponseData create(HttpServletRequest request, HttpServletResponse response, User user) throws Exception{
		if (null == user){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User loginUser = JSON.parseObject(json, User.class); 
		user.setCreator(loginUser.getId());
		//生成ApiKey
		user.setApikey(ApiKeyUtil.makeApiKey());
		return userService.addUser(user);
	}
	
	/**
	 * @Description 修改账号
	 * @param user
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改账号", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "user", value = "账号实体项", dataType = "User")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 2, message="账号已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("user:view")
	public ResponseData update(User user) throws Exception{
		if (null == user){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return userService.editUser(user);
	}
	
	/**
	 * @Description 删除账号
	 * @param userId
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除账号", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "账号ID", required = true, dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("user:view")
	public ResponseData delete(int userId) throws Exception{
		return userService.deleteUser(userId);
	}
	
	/**
	 * @Description 修改密码
	 * @param userId 账号id
	 * @param pwd 密码
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改密码", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "账号ID", dataType = "int"),
    	@ApiImplicitParam(name = "pwd", value = "密码", dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/updatePassword")
	@RequiredPermission("user:view")
	public ResponseData updatePassword(int userId, String pwd) throws Exception{
		if (null == pwd || pwd.isEmpty()){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return userService.setPassword(userId, pwd);
	}
	
	/**
	 * @Description 获取账号角色列表
	 * @param userId 账号Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4号
	 * @Author hxl
	 */
	@ApiOperation(value = "获取账号角色列表", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "账号ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getUserRole")
	@RequiredPermission("user:view")
	public ResponseData getUserRole(int userId){
		List<Role> roleList = userService.getUserRole(userId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", roleList);
	}
	
	/**
	 * @Description 设置账号角色
	 * @param userId 账号ID
	 * @param roleIdStr 逗号拼接的角色ID字符串
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "设置账号角色", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "账号ID", dataType = "int"),
    	@ApiImplicitParam(name = "roleIdStr", value = "逗号拼接的角色ID字符串", dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="设置成功"),
	    @ApiResponse(code = 0, message="设置失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/updateUserRole")
	@RequiredPermission("user:view")
	public ResponseData updateUserRole(int userId, String roleIdStr) throws Exception{
		if (null == roleIdStr){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		String[] roleIdArray = roleIdStr.split(",");
		List<Integer> roleIdList = new ArrayList<Integer>();
		for (String s : roleIdArray){
			roleIdList.add(Integer.parseInt(s));
		}
		return userService.setUserRole(userId, roleIdList);
	}
}
