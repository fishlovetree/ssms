package com.ws.ssms.business.service;

import java.util.List;

import com.ws.ssms.business.model.Customer;

public interface CustomerService {

	/**
	 * @Description 查询客户列表
	 * @param apikey 数据权限
	 * @return
	 * @throws Exception
	 * @Time: 2019年11月12日
	 * @Author hxl
	 */
	public List<Customer> getList(String apikey);
}
