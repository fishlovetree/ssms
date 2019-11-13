package com.ws.ssms.business.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ws.ssms.business.config.RequiredPermission;
import com.ws.ssms.business.model.Customer;
import com.ws.ssms.business.model.User;
import com.ws.ssms.business.service.CustomerService;
import com.ws.ssms.business.util.ResponseData;
import com.ws.ssms.business.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/customer")
@Api(tags="客户管理类")
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	/**
	 * @Description 获取客户列表
	 * @return
	 * @Time 2019年11月12日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取客户列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getList")
	@RequiredPermission("customer:view")
	public ResponseData getList(HttpServletRequest request){
		// 从session获取用户
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<Customer> list = customerService.getList(user.getApikey());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", list);
	}
}
