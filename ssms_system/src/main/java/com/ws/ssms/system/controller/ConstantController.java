package com.ws.ssms.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ws.ssms.system.config.RequiredPermission;
import com.ws.ssms.system.model.Constant;
import com.ws.ssms.system.model.ConstantDetail;
import com.ws.ssms.system.service.ConstantService;
import com.ws.ssms.system.util.ResponseData;
import com.ws.ssms.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/constant")
@Api(tags="常量管理类")
public class ConstantController {
	
	@Autowired
	ConstantService constantService;

	/**
	 * @Description 获取常量集合
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取常量列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("getConstantList")
	@RequiredPermission("constant:view")
	public ResponseData getConstantList() {
		List<Constant> list = constantService.getConstantList();
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", list);
	}
	
	/**
	 * @Description 获取常量子项集合
	 * @Param coding 常量编码
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "获取常量子项列表", httpMethod = "GET")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "coding", value = "常量编码", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("getDetailList")
	@RequiredPermission("constant:view")
	public ResponseData getDetailList(int coding) {
		List<ConstantDetail> list = constantService.getDetailList(coding);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", list);
	}
	
	/**
	 * @Description 添加常量
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加常量", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "constant", value = "常量实体项", dataType = "Constant")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 0, message="常量已存在")
	})
	@PostMapping("addConstant")
	@RequiredPermission("constant:view")
	public ResponseData addConstant(Constant constant)  throws Exception{
		return constantService.addConstant(constant);
	}
	
	/**
	 * @Description 修改常量
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改常量", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "constant", value = "常量实体项", dataType = "Constant")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 0, message="常量已存在")
	})
	@PostMapping("updateConstant")
	@RequiredPermission("constant:view")
	public ResponseData updateConstant(Constant constant)  throws Exception{
		return constantService.editConstant(constant);
	}
	
	/**
	 * @Description 删除常量
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除常量", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "coding", value = "常量编码", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("deleteConstant")
	@RequiredPermission("constant:view")
	public ResponseData deleteConstant(int coding)  throws Exception{
		return constantService.deleteConstant(coding);
	}
	
	/**
	 * @Description 添加常量子项
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "添加常量子项", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "constantDetail", value = "常量子项实体项", dataType = "ConstantDetail")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 0, message="常量子项已存在")
	})
	@PostMapping("addConstantDetail")
	@RequiredPermission("constant:view")
	public ResponseData addConstantDetail(ConstantDetail constantDetail)  throws Exception{
		return constantService.addDetail(constantDetail);
	}
	
	/**
	 * @Description 修改常量子项
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "修改常量子项", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "constantDetail", value = "常量子项实体项", dataType = "ConstantDetail")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 0, message="常量子项已存在")
	})
	@PostMapping("updateConstantDetail")
	@RequiredPermission("constant:view")
	public ResponseData updateConstantDetail(ConstantDetail constantDetail)  throws Exception{
		return constantService.editDetail(constantDetail);
	}
	
	/**
	 * @Description 删除常量子项
	 * @return
	 * @Time 2019年11月9日
	 * @Author hxl
	 */
	@ApiOperation(value = "删除常量子项", httpMethod = "POST")
	@ApiImplicitParams({
    	@ApiImplicitParam(name = "detailId", value = "常量子项ID", dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("deleteConstantDetail")
	@RequiredPermission("constant:view")
	public ResponseData deleteConstantDetail(int detailId)  throws Exception{
		return constantService.deleteDetail(detailId);
	}
}
