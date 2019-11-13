package com.ws.ssms.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.config.LogAspect;
import com.ws.ssms.system.mapper.UserMapper;
import com.ws.ssms.system.model.Role;
import com.ws.ssms.system.model.User;
import com.ws.ssms.system.service.UserService;
import com.ws.ssms.system.util.MD5Util;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;
	
	@Resource
	private LogAspect log;

	/**
	 * @Description 根据账号名获取账号信息
	 * @param username 账号名
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public User getUserByName(String username) {
		return userMapper.selectByUserName(username);
	}

	/**
	 * @Description 获取账号集合
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<User> getUserList(Integer usertype, String organizationcode, String username) {
		return userMapper.selectUserList(usertype, organizationcode, username);
	}
	
	/**
	 * @Description 获取账号一页数据
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月7日
	 * @Author hxl
	 */
	@Override
	public List<User> getUserPage(Integer usertype, String organizationcode, String username, int startindex, int size) {
		return userMapper.selectUserPage(usertype, organizationcode, username, startindex, size);
	}
	
	/**
	 * @Description 获取账号分页数据总数
	 * @param usertype 1-前台账号，0-后台账号
	 * @param organizationcode 登录账号组织机构编码
	 * @param username 账号名
	 * @return
	 * @Time 2019年11月7日
	 * @Author hxl
	 */
	@Override
	public int getUserCount(Integer usertype, String organizationcode, String username) {
		return userMapper.selectUserCount(usertype, organizationcode, username);
	}

	/**
	 * @Description 添加账号
	 * @param user
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData addUser(User user) throws Exception {
		int count = userMapper.selectCountByName(user.getUsername(), 0);
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "账号已存在", null);
		}
		user.setPassword(MD5Util.MD5(user.getPassword())); // 密码使用MD5加密
		int result = userMapper.insertSelective(user);
		if (result > 0){
			//记录操作日志
	        String content = "账号：" + user.getUsername();
	        log.addLog("", "添加账号", content, 0);
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改账号
	 * @param user
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData editUser(User user) throws Exception {
		int count = userMapper.selectCountByName(user.getUsername(), user.getId());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "账号已存在", null);
		}
		int result = userMapper.updateByPrimaryKeySelective(user);
		if (result > 0){
			//记录操作日志
	        String content = "账号：" + user.getUsername();
	        log.addLog("", "修改账号", content, 2);
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 通过id删除账号
	 * @param userid 账号id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteUser(int userid) throws Exception {
		User user = userMapper.selectByPrimaryKey(userid);
		int result = userMapper.deleteByPrimaryKey(userid);
		if (result > 0){
			//记录操作日志
	        String content = "账号：" + user.getUsername();
	        log.addLog("", "删除账号", content, 1);
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}
	
	/**
	 * @Description 修改密码
	 * @param userid 账号id
	 * @param pwd 密码
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData setPassword(int userid, String pwd)throws Exception{
		User user = userMapper.selectByPrimaryKey(userid);
		int result = userMapper.setPassword(MD5Util.MD5(pwd), userid);
		if (result > 0){
			//记录操作日志
	        String content = "账号：" + user.getUsername();
	        log.addLog("", "修改密码", content, 2);
	        
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "设置成功", null);
		}
		else
			return new ResponseData(ResponseEnum.ERROR.getCode(), "设置失败", null);
	}
	
	/**
	 * @Description 获取账号角色列表
	 * @param userId 账号Id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1号
	 * @Author hxl
	 */
	@Override
	public List<Role> getUserRole(Integer userId){
		return userMapper.selectUserRole(userId);
	}
	
	/**
	 * @Description 设置账号角色
	 * @param userId 账号ID
	 * @param roleIdArray 角色ID集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData setUserRole(Integer userId, List<Integer> roleIdArray)throws Exception{
		User user = userMapper.selectByPrimaryKey(userId);
		int result = userMapper.deleteUserRole(userId);
		if (result > 0){
			result = userMapper.insertUserRole(userId, roleIdArray);
			if (result > 0){
				//记录操作日志
		        String content = "账号：" + user.getUsername() + ", 角色：" + roleIdArray;
		        log.addLog("", "设置账号角色", content, 2);
		        
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
