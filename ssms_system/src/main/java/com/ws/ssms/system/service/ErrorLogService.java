package com.ws.ssms.system.service;

import java.util.List;

import com.ws.ssms.system.model.SysErrorlog;

/**
 * @Description:系统异常日志业务逻辑接口
 * @Author hxl
 * @Time: 2019年10月30日
 */
public interface ErrorLogService {
	
	/**
	 * @Description 插入异常日志
	 * @param log
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	boolean insert(SysErrorlog log)throws Exception;
	
	/**
	 * @Description 获取异常日志集合
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	List<SysErrorlog> getLogList(String organizationcode, String username, String starttime, String endtime, int startindex, int endindex);
	
	/**
	 * @Description 获取异常日志总数
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	int getLogListCount(String organizationcode, String username, String starttime, String endtime);
	
	/**
	 * @Description 获取所有日志集合
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	List<SysErrorlog> getAllLog(String organizationcode, String username, String starttime, String endtime);
	
	/**
	 * @Description 根据日志id获取异常明细
	 * @param logid 日志id
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	SysErrorlog getLogRow(Integer logid)throws Exception;
}