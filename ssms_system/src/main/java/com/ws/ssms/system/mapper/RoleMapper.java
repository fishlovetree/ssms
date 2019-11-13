package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Role;

/**
 * @Description:角色管理
 * @Author hxl
 * @Time: 2019年9月11日
 */
public interface RoleMapper {

    Role selectByPrimaryKey(Integer id);
    
    /**
     * @Description 通过名字查询角色数量
     * @param rolename 角色名称
     * @param id 角色id
     * @param roletype 1-前台角色，0-后台角色
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    public int selectCountByName(@Param(value="rolename")String rolename, @Param(value="id")Integer id, 
    		@Param(value="roletype")Integer roletype);
    
    /**
     * @Description 获取角色列表
     * @param roletype 1-前台角色，0-后台角色
     * @param organizationcode 组织机构编码
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    public List<Role> selectRoleList(@Param(value="roletype")Integer roletype, @Param(value="organizationcode")String organizationcode);
    
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
    public List<Role> selectRolePage(@Param(value="roletype")Integer roletype, @Param(value="organizationcode")String organizationcode,
    		@Param(value="startindex")Integer startindex, @Param(value="size")Integer size);
    
    /**
     * @Description 获取角色数量用于分页
     * @param roletype 1-前台角色，0-后台角色
     * @param organizationcode 组织机构编码
     * @return
     * @Time 2019年11月8日
     * @Author hxl
     */
    public int selectRoleCount(@Param(value="roletype")Integer roletype, @Param(value="organizationcode")String organizationcode);
    
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);
}