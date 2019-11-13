package com.ws.ssms.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.system.model.Constant;

public interface ConstantMapper {
    int deleteByPrimaryKey(Integer coding);

    int insert(Constant record);

    int insertSelective(Constant record);

    Constant selectByPrimaryKey(Integer coding);

    int updateByPrimaryKeySelective(Constant record);

    int updateByPrimaryKey(Constant record);
    
    List<Constant> selectConstantList();
    
    int selectRepeat(@Param("coding") Integer coding, @Param("name") String name);
}