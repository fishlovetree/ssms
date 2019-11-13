package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.SysErrorlog;

public interface SysErrorlogMapper {
    
    SysErrorlog selectByPrimaryKey(Integer logid);
    
    List<SysErrorlog> selectLogs(    		
    		@Param(value = "organizationcode") String organizationcode,
    		@Param("username") String username,
    		@Param("starttime")  String starttime,
    		@Param("endtime")  String endtime,
    		@Param("startindex") int startindex,
    		@Param("endindex") int endindex);
    
    int selectLogsCount(    		
    		@Param(value = "organizationcode") String organizationcode,
    		@Param("username") String username,
    		@Param("starttime")  String starttime,
    		@Param("endtime")  String endtime);
    
    List<SysErrorlog> selectAllLogs(    		
    		@Param(value = "organizationcode") String organizationcode,
    		@Param("username") String username,
    		@Param("starttime")  String starttime,
    		@Param("endtime")  String endtime);
    
    int deleteByPrimaryKey(Integer logid);

    int insert(SysErrorlog record);

    int insertSelective(SysErrorlog record);

    int updateByPrimaryKeySelective(SysErrorlog record);

    int updateByPrimaryKey(SysErrorlog record);
}