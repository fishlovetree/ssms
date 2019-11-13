package com.ws.ssms.system.service;

import java.util.List;

import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.util.ResponseData;

public interface UserService {

	/**
	 * @Description 根据账号名获取账号信息
	 * @param username 账号名
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public User getUserByName(String username);
	
	/**
	 * @Description 获取账号集合
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<User> getUserList(Integer usertype, String organizationcode, String username);
	
	/**
	 * @Description 获取账号一页数据
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月7日
	 * @Author hxl
	 */
	public List<User> getUserPage(Integer usertype, String organizationcode, String username, int startindex, int size);
	
	/**
	 * @Description 获取账号分页数据总数
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月7日
	 * @Author hxl
	 */
	public int getUserCount(Integer usertype, String organizationcode, String username);

	/**
	 * @Description 添加账号
	 * @param user
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData addUser(User user) throws Exception;

	/**
	 * @Description 修改账号
	 * @param user
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData editUser(User user) throws Exception;

	/**
	 * @Description 通过id删除账号
	 * @param userid 账号id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData deleteUser(int userid) throws Exception;
	
	/**
	 * @Description 修改密码
	 * @param userid 账号id
	 * @param pwd 密码
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData setPassword(int userid, String pwd) throws Exception;
	
	/**
	 * @Description 获取账号角色列表
	 * @param userId 账号Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1号
	 * @Author hxl
	 */
	public List<Role> getUserRole(Integer userId);
	
	/**
	 * @Description 设置账号角色
	 * @param userId 账号ID
	 * @param roleIdArray 角色ID集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData setUserRole(Integer userId, List<Integer> roleIdArray)throws Exception;

}
