package com.ws.ssms.system.service;

import java.util.List;

import com.ws.ssms.system.model.SysLog;

public interface LogService {

	/**
	 * @Description 插入日志
	 * @param log
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public boolean insert(SysLog log) throws Exception;

	/**
	 * @Description 获取日志集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<SysLog> getLogList(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype, int startindex, int endindex);
	
	/**
	 * @Description 获取日志总数
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public int getLogListCount(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype);

	/**
	 * @Description 获取所有日志集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<SysLog> getAllLogList(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype);
}
