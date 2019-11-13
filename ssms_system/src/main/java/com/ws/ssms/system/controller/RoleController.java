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
import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.RoleService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/role")
@Api(tags="角色管理类")
public class RoleController {

	@Autowired
	RoleService roleService;
	
	/**
	 * @Description 根据登录账号组织机构ID获取角色集合
	 * @param roleType 1-前台角色，0-后台角色
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取角色列表", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "roletype", value = "角色类型：1-前台角色，0-后台角色", dataType = "Integer")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getRoleList")
	@RequiredPermission("role:view")
	public ResponseData getRoleList(HttpServletRequest request, HttpServletResponse response, Integer roletype) {
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<Role> roleList = roleService.getRoleList(roletype, user.getOrganizationcode());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", roleList);
	}
	
	/**
	 * @Description 分页获取角色
	 * @param roletype 1-前台角色，0-后台角色
	 * @param page 页数
	 * @param size 每页行数
	 * @return
	 * @throws Exception
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	@ApiOperation(value = "分页获取角色", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "roletype", value = "角色类型：1-前台角色，0-后台角色", dataType = "Integer"),
    	@ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
    	@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getRolePage")
	@RequiredPermission("role:view")
	public ResponseData getRolePage(HttpServletRequest request, HttpServletResponse response, Integer roletype, int page, int size){
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User user = JSON.parseObject(json, User.class); 
		List<Role> roleList = roleService.getRolePage(roletype, user.getOrganizationcode(), (page - 1) * size, size);
		int total = roleService.getRoleCount(roletype, user.getOrganizationcode());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", roleList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 添加角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加角色", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "role", value = "角色对象", dataType = "Role")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("create")
	@RequiredPermission("role:view")
	public ResponseData create(HttpServletRequest request, HttpServletResponse response, Role role) throws Exception{
		if (null == role){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		HttpSession session = request.getSession();
		String json = (String)session.getAttribute("ws_admin_user");
		User loginUser = JSON.parseObject(json, User.class); 
		role.setCreator(loginUser.getId());
		return roleService.addRole(role);
	}
	
	/**
	 * @Description 修改角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改角色", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "role", value = "角色对象", dataType = "Role")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("update")
	@RequiredPermission("role:view")
	public ResponseData update(Role role) throws Exception{
		if (null == role){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return roleService.editRole(role);
	}
	
	/**
	 * @Description 删除角色
	 * @param roleId 角色id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除角色", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "roleId", value = "角色ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("delete")
	@RequiredPermission("role:view")
	public ResponseData delete(int roleId) throws Exception{
		return roleService.deleteRole(roleId);
	}
	
	/**
	 * @Description 获取角色菜单列表
	 * @param roleId 角色Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4号
	 * @Author hxl
	 */
	@ApiOperation(value = "获取角色菜单列表", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "roleId", value = "角色ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getRoleMenu")
	@RequiredPermission("role:view")
	public ResponseData getRoleMenu(int roleId) {
		List<Menu> menuList = roleService.getRoleMenu(roleId);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuList);
	}
	
	/**
	 * @Description 设置角色菜单
	 * @param roleId 角色ID
	 * @param menuIdStr 逗号拼接的菜单ID集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "设置角色菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "roleId", value = "角色ID", dataType = "int"),
    	@ApiImplicitParam(name = "menuIdStr", value = "逗号拼接的菜单ID字符串", dataType = "String")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="设置成功"),
	    @ApiResponse(code = 0, message="设置失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("updateRoleMenu")
	@RequiredPermission("role:view")
	public ResponseData updateRoleMenu(int roleId, String menuIdStr) throws Exception{
		if (null == menuIdStr){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		String[] menuIdArray = menuIdStr.split(",");
		List<Integer> menuIdList = new ArrayList<Integer>();
		for (String s : menuIdArray){
			menuIdList.add(Integer.parseInt(s));
		}
		return roleService.setRoleMenu(roleId, menuIdList);
	}
}
