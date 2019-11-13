package com.ws.ssms.system.service;

import java.util.List;
import java.util.Map;

import com.ws.ssms.system.model.Constant;
import com.ws.ssms.system.model.ConstantDetail;
import com.ws.ssms.system.util.ResponseData;

public interface ConstantService {

	/**
	 * @Description 获取常量集合
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public List<Constant> getConstantList();
	
	/**
	 * @Description 获取常量子项集合
	 * @Param coding 常量编码
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public List<ConstantDetail> getDetailList(int coding);
	
	/**
	 * @Description 添加常量
	 * @param constant
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData addConstant(Constant constant) throws Exception;

	/**
	 * @Description 修改常量
	 * @param constant
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData editConstant(Constant constant) throws Exception;

	/**
	 * @Description 通过coding删除常量
	 * @param coding 常量编码
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData deleteConstant(int coding) throws Exception;
	
	/**
	 * @Description 添加常量子项
	 * @param constantDetail
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData addDetail(ConstantDetail constantDetail) throws Exception;

	/**
	 * @Description 编辑常量子项
	 * @param constantDetail
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData editDetail(ConstantDetail constantDetail) throws Exception;

	/**
	 * @Description 通过id删除常量子项
	 * @param id 常量子项ID
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public ResponseData deleteDetail(int id) throws Exception;
	
	/**
	 * @Description 常量树
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public List<Map<String, Object>> getTreeData();
	
	/**
	 * @Description 获取常量子项集合(根据上级常量子项值进行归类)
	 * @Param coding 常量编码
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public Map<String, List<ConstantDetail>> getDetailMap(int coding);
}
