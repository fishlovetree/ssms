package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.ConstantDetail;

public interface ConstantDetailMapper {
	
    int deleteByPrimaryKey(Integer detailid);

    int insert(ConstantDetail record);

    int insertSelective(ConstantDetail record);

    ConstantDetail selectByPrimaryKey(Integer detailid);
    
    ConstantDetail selectByDetailValue(@Param("coding") Integer coding, @Param("detailvalue") String detailvalue);
    
    ConstantDetail selectByDetailName(@Param("coding") Integer coding, @Param("detailname") String detailname);
    
    int updateByPrimaryKeySelective(ConstantDetail record);

    int updateByPrimaryKey(ConstantDetail record);
    
    List<ConstantDetail> selectListByCoding(Integer coding);
    
    List<ConstantDetail> selectList();
    
    int selectRepeat(@Param("coding") Integer coding, @Param("detailId") Integer detailId, 
    		@Param("detailvalue") String detailvalue, @Param("detailname") String detailname);
}