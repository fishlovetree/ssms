package com.ws.ssms.system.service;

import java.util.List;

import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.util.ResponseData;

/**
 * @Description:角色业务接口
 * @Author hxl
 * @Time: 2019年11月1日
 */
public interface RoleService {
	
	/**
	 * @Description 根据登录账号组织机构ID获取角色集合
	 * @param roleType 1-前台角色，0-后台角色
	 * @param organizationcode 登录账号组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Role> getRoleList(Integer roleType, String organizationcode);
	
	/**
     * @Description 分页获取角色
     * @param roletype 1-前台角色，0-后台角色
     * @param organizationcode 组织机构编码
     * @param startindex 起始序号
     * @param size 每页数量
     * @return
     * @Time 2019年11月8日
     * @Author hxl
     */
	public List<Role> getRolePage(Integer roletype, String organizationcode, int startIndex, int size);
	
	/**
     * @Description 获取角色数量用于分页
     * @param roletype 1-前台角色，0-后台角色
     * @param organizationcode 组织机构编码
     * @return
     * @Time 2019年11月8日
     * @Author hxl
     */
	public int getRoleCount(Integer roletype, String organizationcode);
	
	/**
	 * @Description 添加角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData addRole(Role role)throws Exception;
	
	/**
	 * @Description 修改角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData editRole(Role role)throws Exception;
	
	/**
	 * @Description 删除角色
	 * @param roleId
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData deleteRole(Integer roleId)throws Exception;
	
	/**
	 * @Description 获取角色菜单列表
	 * @param roleId 角色Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1号
	 * @Author hxl
	 */
	public List<Menu> getRoleMenu(Integer roleId);
	
	/**
	 * @Description 设置角色菜单
	 * @param roleId 角色ID
	 * @param menuIdArray 菜单ID集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData setRoleMenu(Integer roleId, List<Integer> menuIdArray)throws Exception;
}
