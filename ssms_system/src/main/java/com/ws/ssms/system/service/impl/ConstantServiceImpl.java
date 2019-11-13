package com.ws.ssms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ws.ssms.system.config.LogAspect;
import com.ws.ssms.system.mapper.ConstantDetailMapper;
import com.ws.ssms.system.mapper.ConstantMapper;
import com.ws.ssms.system.model.Constant;
import com.ws.ssms.system.model.ConstantDetail;
import com.ws.ssms.system.service.ConstantService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

@Service
public class ConstantServiceImpl implements ConstantService {

	@Resource
	private ConstantMapper constantMapper;

	@Resource
	private ConstantDetailMapper constantDetailMapper;
	
	@Resource
	private LogAspect log;

	/**
	 * @Description 获取常量集合
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public List<Constant> getConstantList() {
		return constantMapper.selectConstantList();
	}

	/**
	 * @Description 获取常量子项集合
	 * @Param coding 常量编码
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public List<ConstantDetail> getDetailList(int coding) {
		return constantDetailMapper.selectListByCoding(coding);
	}

	/**
	 * @Description 添加常量
	 * @param constant
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData addConstant(Constant constant) throws Exception {
		int count = constantMapper.selectRepeat(0, constant.getName());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "常量已存在", null);
		}
		int result = constantMapper.insertSelective(constant);
		if (result > 0){
			//记录操作日志
	        String content = "常量名称：" + constant.getName();
	        log.addLog("", "添加常量", content, 0);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改常量
	 * @param constant
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData editConstant(Constant constant) throws Exception {
		int count = constantMapper.selectRepeat(constant.getCoding(), constant.getName());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "常量已存在", null);
		}
		int result = constantMapper.updateByPrimaryKeySelective(constant);
		if (result > 0){
			//记录操作日志
	        String content = "常量名称：" + constant.getName();
	        log.addLog("", "修改常量", content, 2);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 删除常量
	 * @param coding
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteConstant(int coding) throws Exception {
		Constant constant = constantMapper.selectByPrimaryKey(coding);
		int result = constantMapper.deleteByPrimaryKey(coding);
		if (result > 0){
			//记录操作日志
	        String content = "常量名称：" + constant.getName();
	        log.addLog("", "删除常量", content, 1);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}

	/**
	 * @Description 添加常量子项
	 * @param constantDetail
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData addDetail(ConstantDetail constantDetail) throws Exception {
		int count = constantDetailMapper.selectRepeat(constantDetail.getCoding(), 0, constantDetail.getDetailvalue(), constantDetail.getDetailname());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "常量子项已存在", null);
		}
		Constant constant = constantMapper.selectByPrimaryKey(constantDetail.getCoding());
		int result = constantDetailMapper.insertSelective(constantDetail);
		if (result > 0){
			//记录操作日志
	        String content = "常量名称：" + constant.getName() + ", 子项名称：" + 
			constantDetail.getDetailname() + ", 子项值：" + constantDetail.getDetailvalue();
	        log.addLog("", "添加常量子项", content, 0);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", null);
		}
	}

	/**
	 * @Description 修改常量子项
	 * @param constantDetail
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData editDetail(ConstantDetail constantDetail) throws Exception {
		int count = constantDetailMapper.selectRepeat(constantDetail.getCoding(), constantDetail.getDetailid(), constantDetail.getDetailvalue(), constantDetail.getDetailname());
		if (count > 0){
			return new ResponseData(ResponseEnum.REPEAT.getCode(), "常量子项已存在", null);
		}
		Constant constant = constantMapper.selectByPrimaryKey(constantDetail.getCoding());
		int result = constantDetailMapper
				.updateByPrimaryKeySelective(constantDetail);
		if (result > 0){
			//记录操作日志
	        String content = "常量名称：" + constant.getName() + ", 子项名称：" + 
			constantDetail.getDetailname() + ", 子项值：" + constantDetail.getDetailvalue();
	        log.addLog("", "修改常量子项", content, 2);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", null);
		}
	}

	/**
	 * @Description 通过id删除常量子项
	 * @param id 常量子项ID
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public ResponseData deleteDetail(int id) throws Exception {
		ConstantDetail constantDetail = constantDetailMapper.selectByPrimaryKey(id);
		int result = constantDetailMapper.deleteByPrimaryKey(id);
		if (result > 0){
			//记录操作日志
	        String content = "子项名称：" + constantDetail.getDetailname() + ", 子项值：" + constantDetail.getDetailvalue();
	        log.addLog("", "删除常量子项", content, 1);
	        return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", null);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", null);
		}
	}

	/**
	 * @Description 常量树
	 * @return
	 * @throws Exception
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	public List<Map<String, Object>> getTreeData() {
		List<Constant> list = getConstantList();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("state", "open");
		rootMap.put("id", "0");
		rootMap.put("text", "常量列表");
		rootMap.put("level", "1");
		if (null != list){
			List<Map<String, Object>> constantMapList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Constant constant = (Constant) list.get(i);
				Map<String, Object> constantMap = new HashMap<String, Object>();
				constantMap.put("state", "open");
				constantMap.put("id", constant.getCoding());
				constantMap.put("text", constant.getName());
				constantMap.put("parentid", "0");
				constantMap.put("level", "2");
				constantMapList.add(constantMap);
			}
			rootMap.put("children", constantMapList);
		}
		mapList.add(rootMap);
		return mapList;
	}
	
	/**
	 * @Description 获取常量子项集合(根据上级常量子项值进行归类)
	 * @Param coding 常量编码
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@Override
	public Map<String, List<ConstantDetail>> getDetailMap(int coding) {
		List<ConstantDetail> list = constantDetailMapper.selectListByCoding(coding);
		Map<String, List<ConstantDetail>> map = new LinkedHashMap<String, List<ConstantDetail>>();
		for (int i = 0; i < list.size(); i++){
			ConstantDetail item = list.get(i);
			if (map.containsKey(item.getParentvalue())){
				List<ConstantDetail> entry = map.get(item.getParentvalue());
				entry.add(item);
			}
			else{
				List<ConstantDetail> entry = new ArrayList<ConstantDetail>();
				entry.add(item);
				map.put(item.getParentvalue(), entry);
			}
		}
		return map;
	}
}
