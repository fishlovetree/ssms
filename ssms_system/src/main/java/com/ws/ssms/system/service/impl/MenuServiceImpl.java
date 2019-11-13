package com.ws.ssms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.config.LogAspect;
import com.ws.ssms.system.mapper.MenuMapper;
import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.service.MenuService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

/**
 * @Description:菜单业务接口实现类
 * @Author hxl
 * @Time: 2019年11月1日
 */
@Service
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuMapper menuMapper;
	
	@Resource
	private LogAspect log;
	
	/**
	 * @Description 获取菜单集合
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<Menu> getMenuList(Integer menuType) {
		return menuMapper.selectMenuList(menuType);
	}
	
	/**
	 * @Description 获取菜单分页
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @param startIndex 起始序号
	 * @param size 每页数量
	 * @return
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	@Override
	public List<Menu> getMenuPage(Integer menuType, Integer startIndex, Integer size) {
		return menuMapper.selectMenuPage(menuType, startIndex, size);
	}
	
	/**
	 * @Description 获取菜单总数用于分页
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月8日
	 * @Author hxl
	 */
	@Override
	public int getMenuCount(Integer menuType) {
		return menuMapper.selectMenuCount(menuType);
	}
	
	/**
	 * @Description 获取菜单树
	 * @param menuId 菜单id
	 * @param menuType 1-前台系统菜单，0-后台系统菜单
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<Map<String, Object>> getMenuTree(Integer menuId, Integer menuType){
		List<Menu> menus = menuMapper.selectMenuChilren(menuId, menuType);
		return createTree(menus);
	}
	
	/**
	 * @Description 根据账号获取菜单集合（用于权限判断）
	 * @param userId 账号ID
	 * @param userType 账号类别：1-前台，0-后台
	 * @param userLevel 账号级别：1-普通，0-超级
	 * @return
	 * @throws Exception
	 * @Time 2019年11月4号
	 * @Author hxl
	 */
	@Override
	public List<Menu> getUserMenuList(Integer userid, Integer userType, Integer userLevel){
		if (userLevel == 0){
			return menuMapper.selectMenuList(userType);
		}
		return menuMapper.selectUserMenuList(userid);	
	}
	
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
	@Override
	public List<Map<String, Object>> getUserMenuTree(Integer userid, Integer userType, Integer userLevel){
		List<Menu> menus;
		if (userLevel == 0){
			menus = menuMapper.selectMenuList(userType);
		}
		else{
			menus = menuMapper.selectUserMenuList(userid);
		}
		return createTree(menus);
	}
	
	/**
	 * @Description 生成菜单树形结构
	 * @param menus 账号集合
	 * @return
	 * @Time 2019年11月13号
	 * @Author hxl
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createTree(List<Menu> menus){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < menus.size(); i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			Menu item = menus.get(i);
			cmap = setMap(cmap, item);
			cmap.put("state", "open");//默认打开
			cmap.put("children", new ArrayList<Map<String, Object>>());//子级组织机构
			map.put(item.getId(), cmap);
		}
		//节点级联关系
		for (Map.Entry<Integer, Map<String, Object>> entry : map.entrySet()) {
			if (map.containsKey(entry.getValue().get("superid"))) {
				Map<String, Object> parent = map.get(entry.getValue().get("superid"));
				((List<Map<String, Object>>)parent.get("children")).add(entry.getValue());
			} else {
				result.add(entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * @Description 设置map集合
	 * @param map
	 * @param menu
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	private Map<String, Object> setMap(Map<String, Object> map, Menu menu) {
		map.put("id", menu.getId()); // id
		map.put("menuid", menu.getId()); // id
		map.put("superid", menu.getSuperid());//上级菜单id
		map.put("menuname", menu.getMenuname());//菜单名称
		map.put("text", menu.getMenuname());//菜单名称
		map.put("menuenname", menu.getMenuenname());//菜单英语名称
		map.put("menuurl", menu.getMenuurl());//菜单链接
		map.put("menuicon", menu.getMenuicon());//菜单图标
		map.put("menuorder", menu.getMenuorder());//菜单排序
		map.put("menutype", menu.getMenutype()); //菜单类型：1-前台菜单，0-后台菜单
		return map;
	}
	
	/**
	 * @Description 添加菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData addMenu(Menu menu) throws Exception {
		int count = menuMapper.selectMenuCountByName(menu.getMenuenname(), 0, menu.getMenutype());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "菜单已存在", null);
		}
		int result = menuMapper.insertSelective(menu);
		if(result == 1) {
			//记录操作日志
	        String content = "菜单名称：" + menu.getMenuname();
	        log.addLog("", "添加菜单", content, 0);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改菜单
	 * @param menu 菜单实体
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData editMenu(Menu menu) throws Exception {
		int count = menuMapper.selectMenuCountByName(menu.getMenuenname(), menu.getId(), menu.getMenutype());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "菜单已存在", null);
		}
		int result = menuMapper.updateByPrimaryKeySelective(menu);
		if(result == 1){
			//记录操作日志
	        String content = "菜单名称：" + menu.getMenuname();
	        log.addLog("", "修改菜单", content, 2);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 删除菜单
	 * @param menuId 菜单id
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteMenu(Integer menuId) throws Exception  {
		Menu menu = menuMapper.selectByPrimaryKey(menuId);
		int result = menuMapper.deleteByPrimaryKey(menuId);
		if(result == 1){
			//记录操作日志
	        String content = "菜单名称：" + menu.getMenuname();
	        log.addLog("", "删除菜单", content, 1);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}
}
