package com.ws.ssms.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.mapper.SysLogMapper;
import com.ws.ssms.system.model.SysLog;
import com.ws.ssms.system.service.LogService;

@Service
public class LogServiceImpl implements LogService {

	@Resource
	private SysLogMapper logMapper;

	/**
	 * @Description 插入日志
	 * @param log
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public boolean insert(SysLog log) throws Exception {
		int result = logMapper.insertSelective(log);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Description 获取日志集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<SysLog> getLogList(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype, int startindex, int endindex) {
		return logMapper.selectLogs(organizationcode, username, starttime, endtime, keyword, 
				operatetype, startindex - 1, endindex - 1);
	}

	/**
	 * @Description 获取日志总数
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public int getLogListCount(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype) {
		return logMapper.selectLogsCount(organizationcode, username, starttime, endtime, keyword, operatetype);
	}
	
	/**
	 * @Description 获取所有日志集合
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public List<SysLog> getAllLogList(String organizationcode, String username, String starttime,
			String endtime, String keyword, String operatetype) {
		return logMapper.selectAllLogs(organizationcode, username, starttime, endtime, keyword, 
				operatetype);
	}

}
