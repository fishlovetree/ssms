package com.ws.ssms.business.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ws.ssms.business.model.Customer;

public interface CustomerMapper {
	
	Customer selectByPrimaryKey(Integer customerId);

	List<Customer> selectList(@Param("apikey")String apikey);
}