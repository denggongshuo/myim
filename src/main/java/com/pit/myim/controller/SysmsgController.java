package com.pit.myim.controller;


import com.pit.myim.entity.Page;
import com.pit.myim.entity.PageData;
import com.pit.myim.service.SysmsgService;
import com.pit.myim.util.BaseController;
import com.pit.myim.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 说明：IM系统消息
 * 作者：FH Admin Q 31-3596-790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/sysmsg")
public class SysmsgController extends BaseController {
	
	@Autowired
	private SysmsgService sysmsgService;
	
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
		pd = new PageData(this.getRequest());
		pd.put("USERNAME", getUsername());
		page.setPd(pd);
		List<PageData> varList = sysmsgService.datalistPage(page);		//列出Sysmsg列表
		map.put("varList", varList);
		map.put("page", page);
		map.put("result", errInfo);										//返回结果
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
		sysmsgService.delete(pd);
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		String errInfo = "success";
		PageData pd = new PageData();		
		pd =  new PageData(this.getRequest());
		String DATA_IDS = pd.getString("DATA_IDS");
		if(Tools.notEmpty(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			sysmsgService.deleteAll(ArrayDATA_IDS);
		}else{
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}

}
