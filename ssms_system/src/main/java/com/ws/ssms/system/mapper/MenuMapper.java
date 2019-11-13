package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Menu;

public interface MenuMapper {
	
	Menu selectByPrimaryKey(Integer id);
	
    List<Menu> selectMenuList(@Param(value="menuType")Integer menuType);
    
    List<Menu> selectMenuPage(@Param(value="menuType")Integer menuType, @Param(value="startindex")Integer startindex, @Param(value="size")Integer size);
    
    int selectMenuCount(@Param(value="menuType")Integer menuType);
    
    List<Menu> selectMenuChilren(@Param(value="superId")Integer superId, @Param(value="menuType")Integer menuType);
    
    int selectMenuCountByName(@Param(value="menuName")String menuName, @Param(value = "menuId")Integer menuId, 
    		@Param(value="menuType")Integer menuType);
    
    List<Menu> selectUserMenuList(@Param(value="userid")Integer userid);

    int deleteByPrimaryKey(Integer id);

    int insert(Menu record);

    int insertSelective(Menu record);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}