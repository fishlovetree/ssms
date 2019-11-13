package com.ws.ssms.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.config.LogAspect;
import com.ws.ssms.system.mapper.RoleMapper;
import com.ws.ssms.system.mapper.RoleMenuMapper;
import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.service.RoleService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

/**
 * @Description:角色接口实现类
 * @Author hxl
 * @Time: 2019年11月1日
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleMapper roleMapper;
	
	@Resource
	private RoleMenuMapper roleMenuMapper;
	
	@Resource
	private LogAspect log;

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
	@Override
	public List<Role> getRolePage(Integer roletype, String organizationcode, int startIndex, int size) {
		return roleMapper.selectRolePage(roletype, organizationcode, startIndex, size);
	}
	
	/**
     * @Description 获取角色数量用于分页
     * @param roletype 1-前台角色，0-后台角色
     * @param organizationcode 组织机构编码
     * @return
     * @Time 2019年11月8日
     * @Author hxl
     */
	@Override
	public int getRoleCount(Integer roletype, String organizationcode) {
		return roleMapper.selectRoleCount(roletype, organizationcode);
	}
	
	/**
	 * @Description 根据登录账号组织机构ID获取角色集合
	 * @param roleType 1-前台角色，0-后台角色
	 * @param organizationcode 登录账号组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<Role> getRoleList(Integer roletype, String organizationcode) {
		return roleMapper.selectRoleList(roletype, organizationcode);
	}

	/**
	 * @Description 添加角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData addRole(Role role) throws Exception {
		int result = roleMapper.insert(role);
		if(result == 1) {
			//记录操作日志
	        String content = "角色名称：" + role.getRolename();
	        log.addLog("", "添加角色", content, 0);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改角色
	 * @param role
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData editRole(Role role) throws Exception {
		int result = roleMapper.updateByPrimaryKeySelective(role);
		if(result == 1) {
			//记录操作日志
	        String content = "角色名称：" + role.getRolename();
	        log.addLog("", "修改角色", content, 2);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 删除角色
	 * @param roleId
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteRole(Integer roleId) throws Exception {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		int result = roleMapper.deleteByPrimaryKey(roleId);
		if(result == 1) {
			//记录操作日志
	        String content = "角色名称：" + role.getRolename();
	        log.addLog("", "删除角色", content, 1);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}

	/**
	 * @Description 获取角色菜单列表
	 * @param roleId 角色Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1号
	 * @Author hxl
	 */
	@Override
	public List<Menu> getRoleMenu(Integer roleId) {
		return roleMenuMapper.selectRoleMenu(roleId);
	}

	/**
	 * @Description 设置角色菜单
	 * @param roleId 角色ID
	 * @param menuIdArray 菜单ID集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData setRoleMenu(Integer roleId, List<Integer> menuIdArray) throws Exception {
		Role role = roleMapper.selectByPrimaryKey(roleId);
		int result = roleMenuMapper.deleteByRoleId(roleId);
		if (result > 0){
			result = roleMenuMapper.insertRoleMenu(roleId, menuIdArray);
			if (result > 0){
				//记录操作日志
		        String content = "角色名称：" + role.getRolename();
		        log.addLog("", "设置角色菜单", content, 2);
		        
				return new ResponseData(ResponseEnum.SUCCESS.getCode(), "设置成功", null);
			}
			else{
				return new ResponseData(ResponseEnum.ERROR.getCode(), "设置失败", null);
			}
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "设置失败", null);
		}
	}
}
