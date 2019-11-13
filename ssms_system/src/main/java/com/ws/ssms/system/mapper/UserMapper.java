package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.model.User;

public interface UserMapper {
    
    User selectByPrimaryKey(@Param(value = "id")Integer id);
    
    User selectByUserName(String username);
    
    /**
     * @Description 根据条件获取账号集合
     * @return
     * @Time 2019年11月1日
     * @Author hxl
     */
    List<User> selectUserList(@Param(value = "usertype")Integer usertype, @Param(value = "organizationcode")String organizationcode, 
    		@Param(value = "username")String username);
    
    /**
     * @Description 根据条件获取账号分页数据
     * @return
     * @Time 2019年11月7日
     * @Author hxl
     */
    List<User> selectUserPage(@Param(value = "usertype")Integer usertype, @Param(value = "organizationcode")String organizationcode, 
    		@Param(value = "username")String username, @Param("startindex") Integer startindex, @Param("size") Integer size);
    
    /**
     * @Description 根据条件获取账号分页总数量
     * @return
     * @Time 2019年11月7日
     * @Author hxl
     */
    int selectUserCount(@Param(value = "usertype")Integer usertype, @Param(value = "organizationcode")String organizationcode, 
    		@Param(value = "username")String username);
    
    int selectCountByName(@Param(value = "username")String username, @Param(value = "id")Integer id);
    
    int deleteByPrimaryKey(@Param(value = "id")Integer id);

    int insert(User record);

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    int setPassword(@Param(value = "password")String password, @Param(value = "id")Integer id);
    
    List<Role> selectUserRole(int userid);
    
    int deleteUserRole(int userid);
    
    int insertUserRole(@Param(value = "userid")int userid, @Param(value = "roles")List<Integer> roles);
	
}