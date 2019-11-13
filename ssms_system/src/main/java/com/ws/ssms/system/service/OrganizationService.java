package com.ws.ssms.system.service;

import java.util.List;
import java.util.Map;

import com.ws.ssms.system.model.Organization;
import com.ws.ssms.system.util.ResponseData;

/**
 * @Description: 组织机构业务接口
 * @Author hxl
 * @Time: 2019年11月1日
 */
public interface OrganizationService {
	
	/**
	 * @Description 获取组织机构列表
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Organization> getList(String organizationcode);
	
	/**
	 * @Description 获取组织机构树
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Map<String, Object>> getOrganizationTree(String organizationcode);
	
	/**
	 * @Description 获取组织机构子集编码
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月12日
	 * @Author hxl
	 */
	public List<String> getChildrenCode(String organizationcode);
	
	/**
	 * @Description 添加组织机构
	 * @param organizaiton
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData addOrganization(Organization organization) throws Exception;
	
	/**
	 * @Description 修改组织机构
	 * @param organizaiton
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData editOrganization(Organization organization) throws Exception;
	
	/**
	 * @Description 通过id删除组织机构
	 * @param id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public ResponseData deleteOrganization(int id) throws Exception;
	
}
