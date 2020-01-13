package com.pit.myim.service.impl;

import com.pit.myim.dao.FriendsMapper;
import com.pit.myim.entity.Page;
import com.pit.myim.entity.PageData;
import com.pit.myim.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 说明：好友管理服务接口实现类
 * 作者：FH Admin Q313596790
 * 官网：www.fhadmin.org
 */
@Service
@Transactional //开启事物
public class FriendsServiceImpl implements FriendsService {
	
	@Autowired
	private FriendsMapper friendsMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		friendsMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		friendsMapper.delete(pd);
	}
	
	/**拉黑
	 * @param pd
	 * @throws Exception
	 */
	public void pullblack(PageData pd)throws Exception{
		friendsMapper.pullblack(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		friendsMapper.edit(pd);
	}
	
	/**修改同意状态 
	 * @param pd
	 * @throws Exception
	 */
	public void editAllow(PageData pd)throws Exception{
		friendsMapper.editAllow(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception{
		return friendsMapper.datalistPage(page);
	}
	
	/**列表(全部自己的好友)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return friendsMapper.listAll(pd);
	}
	
	/**列表(添加好友页面检索好友)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllToSearch(PageData pd)throws Exception{
		return friendsMapper.listAllToSearch(pd);
	}
	
	/**列表(全部全部有自己好友的用户)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllFri(PageData pd)throws Exception{
		return friendsMapper.listAllFri(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return friendsMapper.findById(pd);
	}
	
	/**获取某个好友详细信息
	 * @param pd
	 * @throws Exception
	 */
	public PageData getTheFriend(PageData pd)throws Exception{
		return friendsMapper.getTheFriend(pd);
	}
	
	/**获取我的某个好友
	 * @param pd
	 * @throws Exception
	 */
	public PageData findMyFriend(PageData pd)throws Exception{
		return friendsMapper.findMyFriend(pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		friendsMapper.deleteAll(ArrayDATA_IDS);
	}

}
