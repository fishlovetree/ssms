package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Menu;
import com.ws.ssms.system.model.RoleMenuKey;

public interface RoleMenuMapper {
	
    int deleteByPrimaryKey(RoleMenuKey key);
    
    /**
     * @Description 删除角色菜单
     * @param roleid 角色id
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    public int deleteByRoleId(@Param(value="roleid") Integer roleid);

    int insert(RoleMenuKey record);

    int insertSelective(RoleMenuKey record);
    
    /**
     * @Description 批量插入
     * @param roleid 角色id
     * @param menulist 菜单id集合
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    public int insertRoleMenu(@Param(value="roleid") Integer roleid,@Param(value="menulist") List<Integer> menulist);
    
    /**
     * @Description 通过角色id查询拥有的菜单
     * @param roleid 角色id
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    public List<Menu> selectRoleMenu(Integer roleid);
    
}