package com.pit.myim.controller;

import com.pit.myim.entity.Page;
import com.pit.myim.entity.PageData;
import com.pit.myim.service.FgroupService;
import com.pit.myim.util.BaseController;
import com.pit.myim.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：好友分组
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/fgroup")
public class FgroupController extends BaseController {
	
	@Autowired
	private FgroupService fgroupService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object save() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd =  new PageData(this.getRequest());
		pd.put("FGROUP_ID", UuidUtil.get32UUID());			//主键
		pd.put("USERNAME", getUsername());	//用户名
		fgroupService.save(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**删除
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd =  new PageData(this.getRequest());
		fgroupService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@ResponseBody
	public Object edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd =  new PageData(this.getRequest());
		fgroupService.edit(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public Object list(Page page) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd =  new PageData(this.getRequest());
		pd.put("USERNAME", getUsername()); //用户名
		page.setPd(pd);
		List<PageData> varList = fgroupService.datalistPage(page);	//列出Fgroup列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("pd", pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@ResponseBody
	public Object goEdit()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();
		pd = new PageData(this.getRequest());
		pd = fgroupService.findById(pd);	//根据ID读取
		map.put("pd", pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}	
	
	/**获取分组列表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getFgroup")
	@ResponseBody
	public Object getLevels() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		String errInfo = "success";
		pd =  new PageData(this.getRequest());
		pd.put("USERNAME", getUsername());	//用户名
		List<PageData> varList = fgroupService.listAll(pd);
		map.put("list", varList);	
		map.put("result", errInfo);				//返回结果
		return map;
	}

}
