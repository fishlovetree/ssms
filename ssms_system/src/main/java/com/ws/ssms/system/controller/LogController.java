package com.ws.ssms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.system.config.RequiredPermission;
import com.ws.ssms.system.model.SysLog;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.LogService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/sysLog")
@Api(tags="日志管理类")
public class LogController {

	@Autowired
	LogService logService;
	
	/**
	 * @Description 获取所有日志集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取日志列表", httpMethod = "GET")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "username", value = "操作员", dataType = "String"),
    	@ApiImplicitParam(name = "starttime", value = "开始时间", dataType = "String"),
    	@ApiImplicitParam(name = "endtime", value = "结束时间", dataType = "String"),
    	@ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String"),
    	@ApiImplicitParam(name = "operatetype", value = "操作类型", dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getAllLogList")
	@RequiredPermission("sysLog:view")
	public ResponseData getAllLogList(HttpServletRequest request, HttpServletResponse response, String username, String starttime,
			String endtime, String keyword, String operatetype){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<SysLog> logList = logService.getAllLogList(user.getOrganizationcode(), username, 
				starttime, endtime, keyword, operatetype);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", logList);
	}
	
	/**
	 * @Description 获取日志分页
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "分页获取日志列表", httpMethod = "GET")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "username", value = "操作员", dataType = "String"),
    	@ApiImplicitParam(name = "starttime", value = "开始时间", dataType = "String"),
    	@ApiImplicitParam(name = "endtime", value = "结束时间", dataType = "String"),
    	@ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String"),
    	@ApiImplicitParam(name = "operatetype", value = "操作类型", dataType = "String"),
    	@ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
    	@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getLogPage")
	@RequiredPermission("sysLog:view")
	public ResponseData getLogPage(HttpServletRequest request, HttpServletResponse response, String username, String starttime,
			String endtime, String keyword, String operatetype, int page, int size){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<SysLog> logList = logService.getLogList(user.getOrganizationcode(), username, starttime, endtime, keyword, 
				operatetype, (page - 1) * size + 1, page * size);
		int total = logService.getLogListCount(user.getOrganizationcode(), username, starttime, endtime, keyword, 
				operatetype);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", logList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
}
