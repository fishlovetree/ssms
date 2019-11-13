package com.ws.ssms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.config.LogAspect;
import com.ws.ssms.system.mapper.OrganizationMapper;
import com.ws.ssms.system.model.Organization;
import com.ws.ssms.system.service.OrganizationService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	@Resource
	private OrganizationMapper organizationMapper;
	
	@Resource
	private LogAspect log;
	
	/**
	 * @Description 获取组织机构列表
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	public List<Organization> getList(String organizationcode){
		return organizationMapper.selectList(organizationcode);
	}
	
	/**
	 * @Description 获取组织机构树
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getOrganizationTree(String organizationcode){
		List<Organization> organizations = organizationMapper.selectChildren(organizationcode);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<Integer, Map<String, Object>>();
		for (int i = 0; i < organizations.size(); i++){
			Map<String, Object> cmap = new HashMap<String, Object>();
			cmap = setOrganization(cmap, organizations.get(i));
			cmap.put("state", "open");//默认打开
			cmap.put("children", new ArrayList<Map<String, Object>>());//子级组织机构
			map.put(organizations.get(i).getOrganizationid(), cmap);
		}
		//节点级联关系
		for (Map.Entry<Integer, Map<String, Object>> entry : map.entrySet()) {
			if (map.containsKey(entry.getValue().get("parentid"))) {
				Map<String, Object> parent = map.get(entry.getValue().get("parentid"));
				((List<Map<String, Object>>)parent.get("children")).add(entry.getValue());
			} else {
				result.add(entry.getValue());
			}
		}
		return result;
	}
	
	/**
	 * @Description 设置map集合
	 * @param map
	 * @param organization
	 * @return
	 * @Time 2018年1月6日
	 * @Author hxl
	 */
	private Map<String, Object> setOrganization(Map<String, Object> map, Organization organization) {
		map.put("id", organization.getOrganizationid());
		map.put("organizationid", organization.getOrganizationid()); // id
		map.put("organizationcode", organization.getOrganizationcode()); // 组织机构编码
		map.put("organizationname", organization.getOrganizationname()); //组织机构名称
		map.put("text", organization.getOrganizationname()); //组织机构名称
		map.put("parentid", organization.getParentid()); //上级组织机构id
		return map;
	}
	
	/**
	 * @Description 获取组织机构子集编码
	 * @param organizationcode 组织机构编码
	 * @return
	 * @Time 2019年11月12日
	 * @Author hxl
	 */
	public List<String> getChildrenCode(String organizationcode){
		return organizationMapper.selectChildrenCode(organizationcode);
	}

	/**
	 * @Description 添加组织机构
	 * @param organizaiton
	 * @return
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData addOrganization(Organization organization) throws Exception{
		int count = organizationMapper.selectCountByCode(organization.getOrganizationcode(), 0);
		if (count > 0) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "组织机构已经存在", null);
		}
		int result = organizationMapper.insertSelective(organization);
		if (result > 0){
			log.addLog("","添加组织机构", "组织机构名称："+organization.getOrganizationname(), 0);
			
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改组织机构
	 * @param organizaiton
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData editOrganization(Organization organization) throws Exception{
		int count = organizationMapper.selectCountByCode(organization.getOrganizationcode(), organization.getOrganizationid());
		if (count > 0) {
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "组织机构已经存在", null);
		}
		int result = organizationMapper.updateByPrimaryKeySelective(organization);
		if (result > 0){
			log.addLog("","修改组织机构", "组织机构名称："+organization.getOrganizationname(), 2);
			
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 通过id删除组织机构
	 * @param id
	 * @return
	 * @throws Exception
	 * @Time 2019年11月1日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteOrganization(int id) throws Exception{
		Organization organization = organizationMapper.selectByPrimaryKey(id);
		int result = organizationMapper.deleteByPrimaryKey(id);
		if (result > 0){
			log.addLog("","删除组织机构", "组织机构名称："+organization.getOrganizationname(), 1);
			
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}
}
