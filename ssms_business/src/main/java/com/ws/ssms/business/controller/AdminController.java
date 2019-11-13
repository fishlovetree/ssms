package com.ws.ssms.business.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ws.ssms.business.util.ResponseData;
import com.ws.ssms.business.util.ResponseEnum;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

	@GetMapping("/notlogon")
    public ResponseData notlogon(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new ResponseData(ResponseEnum.NOTLOGON.getCode(), "未登录", "");
    }
    
    @GetMapping("/unauth")
    public ResponseData unauth(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new ResponseData(ResponseEnum.UNAUTH.getCode(), "没有权限", "");
    }
}
