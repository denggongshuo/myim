package com.pit.myim.controller;

import com.pit.myim.entity.PageData;
import com.pit.myim.entity.UserEntity;
import com.pit.myim.service.UserService;
import com.pit.myim.util.BaseController;
import com.pit.myim.util.Const;
import com.pit.myim.util.IniFileUtil;
import com.pit.myim.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;


/**
 * 说明：系统设置、初始信息等
 * 作者：FH Admin Q 31 3596790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/head")
public class HeadController extends BaseController {

	@Autowired
	private UserService userService;

	/**获取基本信息
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/getInfo", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object getList() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		String errInfo = "success";
		UserEntity userEntity = userService.getUserByUserName(getUsername());
		if(null != userEntity){
			pd.put(Const.SESSION_USERNAME, userEntity.getUsername());
			map.put("userPhoto","/myim/"+userEntity.getUserphotoUrl());//用户头像
			map.put("NAME", userEntity.getName());
			map.put("USERNAME", userEntity.getUsername());

			//String infFilePath = PathUtil.getClasspath()+Const.SYSSET;								//配置文件路径
			String infFilePath=java.net.URLDecoder.decode(PathUtil.getClasspath()+ Const.SYSSET,"utf-8");//配置文件路径
			
			String onlineIp = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "onlineIp", "172.18.1.85");		//在线管理IP
			String onlinePort = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "onlinePort", "8869");			//在线管理端口
			map.put("onlineAdress", onlineIp+":"+onlinePort);	//在线管理websocket地址
			
			String imIp = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "imIp", "172.18.1.85");				//即时聊天IP
			String imPort = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "imPort", "8879");					//即时聊天端口
			map.put("wimadress", imIp+":"+imPort);				//即时聊天websocket地址
			
			String sysName = IniFileUtil.readCfgValue(infFilePath, "SysSet1", Const.SYSNAME, "FH Admin");		//系统名称
			map.put(Const.SYSNAME, sysName);
			errInfo = "success";
		}else {
			errInfo = "error";
		}
		map.put("result", errInfo);
		return map;
	}
}
