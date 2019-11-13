package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Organization;

public interface OrganizationMapper {

    Organization selectByPrimaryKey(Integer organizationid);
    
    Organization selectBycode(@Param(value = "organizationcode")String organizationcode);
    
    List<Organization> selectList(@Param(value = "organizationcode")String organizationcode);
    
    List<Organization> selectChildren(@Param(value = "organizationcode")String organizationcode);
    
    List<String> selectChildrenCode(@Param(value = "organizationcode")String organizationcode);
    
    int selectCountByCode(@Param(value = "organizationcode")String organizationcode, @Param(value = "organizationid")Integer organizationid);
    
    int deleteByPrimaryKey(Integer organizationid);
    
    //删除代理告警方案
    int deleteProxyAlarmPlan(Integer organizationid);

    int insert(Organization record);

    int insertSelective(Organization record);

    int updateByPrimaryKeySelective(Organization record);

    int updateByPrimaryKey(Organization record);
}