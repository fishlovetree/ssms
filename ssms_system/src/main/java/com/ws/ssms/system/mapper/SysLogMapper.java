package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.SysLog;

public interface SysLogMapper {

	SysLog selectByPrimaryKey(Integer logid);

	List<SysLog> selectLogs(@Param(value = "organizationcode") String organizationcode, @Param("username") String username,
			@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("keyword") String keyword,
			@Param("operatetype") String operatetype, @Param("startindex") int startindex, @Param("endindex") int endindex);

	int selectLogsCount(@Param(value = "organizationcode") String organizationcode, @Param("username") String username,
			@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("keyword") String keyword,
			@Param("operatetype") String operatetype);

	List<SysLog> selectAllLogs(@Param(value = "organizationcode") String organizationcode, @Param("username") String username,
			@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("keyword") String keyword,
			@Param("operatetype") String operatetype);
	
	int deleteByPrimaryKey(Integer logid);

	int insert(SysLog record);

	int insertSelective(SysLog record);

	int updateByPrimaryKeySelective(SysLog record);

	int updateByPrimaryKey(SysLog record);

}