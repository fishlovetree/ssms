package com.ws.ssms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ws.ssms.business.mapper.CustomerMapper;
import com.ws.ssms.business.model.Customer;
import com.ws.ssms.business.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerMapper customerMapper;
	
	/**
	 * @Description 查询客户列表
	 * @param apikey 数据权限
	 * @return
	 * @throws Exception
	 * @Time: 2019年11月12日
	 * @Author hxl
	 */
	public List<Customer> getList(String apikey){
		return customerMapper.selectList(apikey);
	}
}