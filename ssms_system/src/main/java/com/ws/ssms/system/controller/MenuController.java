package com.ws.ssms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ws.ssms.system.config.RequiredPermission;
import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.service.MenuService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/menu")
@Api(tags="菜单管理类")
public class MenuController {

	@Autowired
	MenuService menuService;
	
	/**
	 * @Description 获取菜单集合
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取菜单列表", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menuType", value = "菜单类型：1-前台系统菜单，0-后台系统菜单", dataType = "Integer")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getMenuList")
	@RequiredPermission("menu:view")
	public ResponseData getMenuList(Integer menuType){
		List<Menu> menuList = menuService.getMenuList(menuType);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuList);
	}
	
	/**
	 * @Description 分页获取菜单
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @param page 页数
	 * @param size 每页行数
	 * @return
	 * @throws Exception
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	@ApiOperation(value = "分页获取菜单", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menuType", value = "菜单类型：1-前台系统菜单，0-后台系统菜单", dataType = "Integer"),
    	@ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
    	@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getMenuPage")
	@RequiredPermission("menu:view")
	public ResponseData getMenuPage(Integer menuType, int page, int size){
		List<Menu> menuList = menuService.getMenuPage(menuType, (page - 1) * size, size);
		int total = menuService.getMenuCount(menuType);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", menuList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 获取菜单树
	 * @param menuId 菜单id
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取菜单树形结构", httpMethod = "GET")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "Integer"),
    	@ApiImplicitParam(name = "menuType", value = "菜单类型：1-前台系统菜单，0-后台系统菜单", dataType = "Integer")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getMenuTree")
	@RequiredPermission("menu:view")
	public ResponseData getMenuTree(Integer menuId, Integer menuType){
		List<Map<String, Object>> menuTree = menuService.getMenuTree(menuId, menuType);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", menuTree);
	}
	
	/**
	 * @Description 添加菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menu", value = "菜单实体", dataType = "Menu")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 2, message="菜单已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("menu:view")
	public ResponseData create(Menu menu) throws Exception{
		if (null == menu){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return menuService.addMenu(menu);
	}
	
	/**
	 * @Description 修改菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月4日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menu", value = "菜单实体", dataType = "Menu")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 2, message="菜单已存在"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("menu:view")
	public ResponseData update(Menu menu) throws Exception{
		if (null == menu){
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误", null);
		}
		return menuService.editMenu(menu);
	}
	
	/**
	 * @Description 删除菜单
	 * @param menuId 菜单id
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除菜单", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "menuId", value = "菜单ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("menu:view")
	public ResponseData delete(int menuId) throws Exception{
		return menuService.deleteMenu(menuId);
	}
}
