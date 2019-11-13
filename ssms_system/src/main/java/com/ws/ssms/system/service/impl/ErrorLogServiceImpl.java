package com.ws.ssms.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.mapper.SysErrorlogMapper;
import com.ws.ssms.system.model.SysErrorlog;
import com.ws.ssms.system.service.ErrorLogService;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {

	@Resource
	private SysErrorlogMapper logMapper;

	/**
	 * @Description 插入异常日志
	 * @param log
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	@Override
	public boolean insert(SysErrorlog log) throws Exception {
		int result = logMapper.insertSelective(log);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @Description 获取异常日志集合
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	@Override
	public List<SysErrorlog> getLogList(String organizationcode, String username, String starttime, String endtime, int startindex, int endindex) {
		return logMapper.selectLogs(organizationcode, username, starttime, endtime, startindex - 1, endindex - 1);
	}
	
	/**
	 * @Description 获取异常日志总数
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	@Override
	public int getLogListCount(String organizationcode, String username, String starttime, String endtime) {
		return logMapper.selectLogsCount(organizationcode, username, starttime, endtime);
	}
	
	/**
	 * @Description 获取所有日志集合
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	@Override
	public List<SysErrorlog> getAllLog(String organizationcode, String username, String starttime, String endtime) {
		return logMapper.selectAllLogs(organizationcode, username, starttime, endtime);
	}

	/**
	 * @Description 根据日志id获取异常明细
	 * @param logid 日志id
	 * @return
	 * @throws Exception
	 * @Time: 2019年10月30日
	 * @Author hxl
	 */
	@Override
	public SysErrorlog getLogRow(Integer logid) {
		return logMapper.selectByPrimaryKey(logid);
	}
}