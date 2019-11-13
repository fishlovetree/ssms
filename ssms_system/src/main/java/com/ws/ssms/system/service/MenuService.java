package com.ws.ssms.system.service;

import java.util.List;
import java.util.Map;

import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.util.ResponseData;

/**
 * @Description:菜单业务接口
 * @Author hxl
 * @Time: 2019年11月1日
 */
public interface MenuService {

	/**
	 * @Description 获取菜单集合
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Menu> getMenuList(Integer menuType);
	
	/**
	 * @Description 获取菜单分页
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @param startIndex 起始序号
	 * @param size 每页数量
	 * @return
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	public List<Menu> getMenuPage(Integer menuType, Integer startIndex, Integer size);
	
	/**
	 * @Description 获取菜单总数用于分页
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	public int getMenuCount(Integer menuType);
	
	/**
	 * @Description 获取菜单树
	 * @param menuId 菜单id
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Map<String, Object>> getMenuTree(Integer menuId, Integer menuType);
	
	/**
	 * @Description 根据账号获取菜单集合（用于权限判断）
	 * @param userid 账号ID
	 * @param userType 账号类别：1-前台，0-后台
	 * @param userLevel 账号级别：1-普通，0-超级
	 * @return
	 * @throws Exception
	 * @Time 2018年7月25号
	 * @Author hxl
	 */
	public List<Menu> getUserMenuList(Integer userid, Integer userType, Integer userLevel);
	
	/**
	 * @Description 根据账号获取菜单树形结构
	 * @param userId 账号ID
	 * @param userType 账号类别：1-前台，0-后台
	 * @param userLevel 账号级别：1-普通，0-超级
	 * @return
	 * @throws Exception
	 * @Time 2019年11月13号
	 * @Author hxl
	 */
	public List<Map<String, Object>> getUserMenuTree(Integer userid, Integer userType, Integer userLevel);
	
	/**
	 * @Description 添加菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData addMenu(Menu menu)throws Exception;

	/**
	 * @Description 修改菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData editMenu(Menu menu)throws Exception;
	
	/**
	 * @Description 删除菜单
	 * @param menuId 菜单id
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData deleteMenu(Integer menuId)throws Exception;
}
