package com.ws.ssms.system.controller;

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
import com.ws.ssms.system.model.Organization;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.OrganizationService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/organization")
@Api(tags="组织机构管理类")
public class OrganizationController {

	@Autowired
	OrganizationService organizationService;
	
	/**
	 * @Description 获取组织机构列表
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取组织机构列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getList")
	@RequiredPermission("organization:view")
	public ResponseData getList(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<Organization> list = organizationService.getList(user.getOrganizationcode());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", list);
	}
	
	/**
	 * @Description 获取组织机构树
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取组织机构树形列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getOrganizationTree")
	@RequiredPermission("organization:view")
	public ResponseData getOrganizationTree(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<Map<String, Object>> tree = organizationService.getOrganizationTree(user.getOrganizationcode());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", tree);
	}
	
	/**
	 * @Description 添加组织机构
	 * @param organizaiton
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加组织机构", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "organization", value = "组织机构实体项", dataType = "Organization")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 2, message="组织机构已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("organization:view")
	public ResponseData create(HttpServletRequest request, HttpServletResponse response, Organization organization) throws Exception{
		if (null == organization){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		organization.setCompactor(user.getId());
		return organizationService.addOrganization(organization);
	}
	
	/**
	 * @Description 修改组织机构
	 * @param organizaiton
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改组织机构", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "organization", value = "组织机构实体项", dataType = "Organization")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 2, message="组织机构已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("organization:view")
	public ResponseData update(Organization organization) throws Exception{
		if (null == organization){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return organizationService.editOrganization(organization);
	}
	
	/**
	 * @Description 删除组织机构
	 * @param id
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除组织机构", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "组织机构ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("organization:view")
	public ResponseData delete(int id) throws Exception{
		return organizationService.deleteOrganization(id);
	}
}
