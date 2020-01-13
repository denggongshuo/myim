package com.pit.myim.controller;



import com.pit.myim.entity.PageData;
import com.pit.myim.entity.UserEntity;
import com.pit.myim.plugins.websocketInstantMsg.ChatServerPool;
import com.pit.myim.service.*;
import com.pit.myim.util.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;


/**
 * 说明：聊天即时通讯接口
 * 作者：FH Admin Q 31-3596-790
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/iminterface")
public class ImInterfaceController extends BaseController {

    private final String http_root_path = "/upload/";


    @Autowired
    private UserService userService;
    @Autowired
    private ImstateService imstateService;
    @Autowired
    private FriendsService friendsService;
    @Autowired
    private FgroupService fgroupService;
    @Autowired
    private IQgroupService iQgroupService;
    @Autowired
    private QgroupService qgroupService;
    @Autowired
    private SysmsgService sysmsgService;
    @Autowired
    private HismsgService hismsgService;

    /**
     * 获取好友(群组)列表(聊天接口请求)
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    @ApiOperation("获取好友(群组)列表(聊天接口请求)")
    public Object getList() throws Exception {
        PageData pd = new PageData();

        Map<String, Object> zmap = new LinkedHashMap<String, Object>();
        Map<String, Object> nmap = new LinkedHashMap<String, Object>();
        pd = new PageData(this.getRequest());
        pd.put(Const.SESSION_USERNAME, getUsername());        //用户名

        PageData stapd = new PageData();
        stapd = imstateService.findByUsername(pd);

        UserEntity userEntity = userService.getUserByUserName(getUsername());

        Map<String, Object> nowmap = new LinkedHashMap<String, Object>();
        nowmap.put("username", getName());                //当前用户姓名
        nowmap.put("id", getUsername());            //当前用户名
        nowmap.put("status", null == stapd ? "online" : stapd.getString("online"));        //在线状态
        nowmap.put("sign", null == stapd ? "个性签名" : stapd.getString("autograph"));        //个性签名
        nowmap.put("avatar", userEntity.getUserphotoUrl());//当前用户头像

        nmap.put("mine", nowmap);

        List<PageData> fList = friendsService.listAll(pd);                //全部好友
        List<PageData> isGList = new ArrayList<PageData>();                //有分组的好友
        List<PageData> noGList = new ArrayList<PageData>();                //未分组的好友
        for (int i = 0; i < fList.size(); i++) {
            if (null == fList.get(i).get("gname") || "".equals(fList.get(i).getString("gname").trim())) {
                noGList.add(fList.get(i));
            } else {
                isGList.add(fList.get(i));
            }
        }
        List<PageData> groupList = fgroupService.listAll(pd);            //当然用户的全部好友分组
        List<Object> gmapList = new ArrayList<Object>();                //存放每个分组以及分组里面的好友
        for (int i = 0; i < groupList.size(); i++) {
            Map<String, Object> gmap = new LinkedHashMap<String, Object>();
            gmap.put("groupname", groupList.get(i).getString("name"));
            gmap.put("id", groupList.get(i).getString("fgroupId"));
            gmap.put("online", i + 1);
            List<Object> gfList = new ArrayList<Object>();
            List<Object> gfListon = new ArrayList<Object>();            //存放在线的
            List<Object> gfListoff = new ArrayList<Object>();            //存放离线的
            for (int n = 0; n < isGList.size(); n++) {
                if (groupList.get(i).getString("fgroupId").equals(isGList.get(n).getString("fgroupId"))) {
                    Map<String, Object> fpd = new LinkedHashMap<String, Object>();
                    fpd.put("username", isGList.get(n).getString("name"));            //姓名
                    fpd.put("id", isGList.get(n).getString("fusername"));            //用户名
                    fpd.put("avatar", null == isGList.get(n).get("photo2") ? "assets/images/user/avatar-2.jpg" : http_root_path + isGList.get(n).getString("photo2"));        //头像
                    fpd.put("sign", null == isGList.get(n).get("autograph") ? "" : isGList.get(n).getString("autograph"));                                //个性签名
                    if (null != isGList.get(n).get("online") && "hide".equals(isGList.get(n).getString("online"))) {//好友在线状态设置为离线的(不管是否真实在线,都判定离线)
                        fpd.put("status", "offline");                                //离线状态
                        gfListoff.add(fpd);
                    } else {                                                            //如果设置的状态为在线，再去判断真实是否在线
                        Collection<String> user = ChatServerPool.getOnlineUser();    //所有在线用户
                        if (!user.contains(isGList.get(n).getString("fusername"))) {
                            fpd.put("status", "offline");                            //离线状态
                            gfListoff.add(fpd);
                        } else {                                                        //其它情况都是在线状态，在线状态就不用加status了
                            gfListon.add(fpd);
                        }
                    }
                }
            }
            gfList.addAll(gfListon);    //在线的放上面
            gfList.addAll(gfListoff);    //离线的放下面
            gmap.put("list", gfList);
            gmapList.add(gmap);
        }

        Map<String, Object> nogmap = new LinkedHashMap<String, Object>(); //存放无分组的好友(无分组就是默认分组)
        nogmap.put("groupname", "未分组");
        nogmap.put("id", "9999");
        nogmap.put("online", 999);
        List<Object> nogfList = new ArrayList<Object>();
        List<Object> nogfListon = new ArrayList<Object>();                //存放在线的
        List<Object> nogfListoff = new ArrayList<Object>();                //存放离线线的
        for (int n = 0; n < noGList.size(); n++) {
            Map<String, Object> nfpd = new LinkedHashMap<String, Object>();
            nfpd.put("username", noGList.get(n).getString("name"));        //姓名
            nfpd.put("id", noGList.get(n).getString("fusername"));        //用户名
            nfpd.put("avatar", null == noGList.get(n).get("photo2") ? "assets/images/user/avatar-2.jpg" : http_root_path + noGList.get(n).getString("photo2"));        //头像
            nfpd.put("sign", null == noGList.get(n).get("AUTOGRAPH") ? "" : noGList.get(n).getString("autograph"));                                //个性签名
            if (null != noGList.get(n).get("online") && "hide".equals(noGList.get(n).getString("online"))) {//好友在线状态设置为离线的(不管是否真实在线,都判定离线)
                nfpd.put("status", "offline");                                //离线状态
                nogfListoff.add(nfpd);
            } else {                                                            //如果设置的状态为在线，再去判断真实是否在线
                Collection<String> user = ChatServerPool.getOnlineUser();    //所有在线用户
                if (!user.contains(noGList.get(n).getString("fusername"))) {
                    nfpd.put("status", "offline");                            //离线状态
                    nogfListoff.add(nfpd);
                } else {                                                        //其它情况都是在线状态，在线状态就不用加status了
                    nogfListon.add(nfpd);
                }
            }
        }
        nogfList.addAll(nogfListon);    //在线的放上面
        nogfList.addAll(nogfListoff);    //离线的放下面
        if (noGList.size() > 0) {
            nogmap.put("list", nogfList);
            gmapList.add(nogmap);
        }

        nmap.put("friend", gmapList); //好友分组及好友数据

        List<Object> qfList = new ArrayList<Object>();
        List<PageData> myqList = getMyAllQgroup(pd);                    //我在的全部群列表
        for (int i = 0; i < myqList.size(); i++) {
            Map<String, Object> qmap = new LinkedHashMap<String, Object>();
            qmap.put("groupname", myqList.get(i).getString("name"));    //群名称
            qmap.put("id", myqList.get(i).getString("qgroupId"));        //群ID
            qmap.put("avatar", myqList.get(i).getString("photo"));        //群头像
            qfList.add(qmap);
        }
        nmap.put("group", qfList); //群组数据

        zmap.put("code", 0);
        zmap.put("msg", "");
        zmap.put("data", nmap);

        return zmap;
    }

    /**
     * 获取好友(群组)列表(聊天接口请求)[手机端]
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/getListToApp")
    @ResponseBody
    public Object getListToApp() throws Exception {
        PageData pd = new PageData();

        Map<String, Object> nmap = new LinkedHashMap<String, Object>();
        pd = new PageData(this.getRequest());
        pd.put(Const.SESSION_USERNAME, getUsername());        //用户名

        PageData stapd = new PageData();
        stapd = imstateService.findByUsername(pd);
        UserEntity userEntity = userService.getUserByUserName(getUsername());
        Map<String, Object> nowmap = new LinkedHashMap<String, Object>();
        nowmap.put("username", getName());                //当前用户姓名
        nowmap.put("id", getUsername());            //当前用户名
        nowmap.put("status", null == stapd ? "online" : stapd.getString("ONLINE"));        //在线状态
        nowmap.put("sign", null == stapd ? "个性签名" : stapd.getString("AUTOGRAPH"));        //个性签名
        nowmap.put("avatar", userEntity.getUserphotoUrl());//当前用户头像

        nmap.put("mine", nowmap);

        List<PageData> fList = friendsService.listAll(pd);                //全部好友
        List<PageData> isGList = new ArrayList<PageData>();                //有分组的好友
        List<PageData> noGList = new ArrayList<PageData>();                //未分组的好友
        for (int i = 0; i < fList.size(); i++) {
            if (null == fList.get(i).get("GNAME") || "".equals(fList.get(i).getString("GNAME").trim())) {
                noGList.add(fList.get(i));
            } else {
                isGList.add(fList.get(i));
            }
        }
        List<PageData> groupList = fgroupService.listAll(pd);            //当然用户的全部好友分组
        List<Object> gmapList = new ArrayList<Object>();                //存放每个分组以及分组里面的好友
        for (int i = 0; i < groupList.size(); i++) {
            Map<String, Object> gmap = new LinkedHashMap<String, Object>();
            gmap.put("groupname", groupList.get(i).getString("NAME"));
            gmap.put("id", groupList.get(i).getString("FGROUP_ID"));
            gmap.put("online", i + 1);
            List<Object> gfList = new ArrayList<Object>();
            List<Object> gfListon = new ArrayList<Object>();            //存放在线的
            List<Object> gfListoff = new ArrayList<Object>();            //存放离线的
            for (int n = 0; n < isGList.size(); n++) {
                if (groupList.get(i).getString("FGROUP_ID").equals(isGList.get(n).getString("FGROUP_ID"))) {
                    Map<String, Object> fpd = new LinkedHashMap<String, Object>();
                    fpd.put("username", isGList.get(n).getString("NAME"));            //姓名
                    fpd.put("id", isGList.get(n).getString("FUSERNAME"));            //用户名
                    fpd.put("avatar", null == isGList.get(n).get("PHOTO2") ? "assets/images/user/avatar-2.jpg" : isGList.get(n).getString("PHOTO2"));            //头像
                    fpd.put("sign", null == isGList.get(n).get("AUTOGRAPH") ? "" : isGList.get(n).getString("AUTOGRAPH"));                                //个性签名
                    if (null != isGList.get(n).get("ONLINE") && "hide".equals(isGList.get(n).getString("ONLINE"))) {//好友在线状态设置为离线的(不管是否真实在线,都判定离线)
                        fpd.put("status", "offline");                                //离线状态
                        gfListoff.add(fpd);
                    } else {                                                            //如果设置的状态为在线，再去判断真实是否在线
                        Collection<String> user = ChatServerPool.getOnlineUser();    //所有在线用户
                        if (!user.contains(isGList.get(n).getString("FUSERNAME"))) {
                            fpd.put("status", "offline");                            //离线状态
                            gfListoff.add(fpd);
                        } else {                                                        //其它情况都是在线状态，在线状态就不用加status了
                            gfListon.add(fpd);
                        }
                    }
                }
            }
            gfList.addAll(gfListon);    //在线的放上面
            gfList.addAll(gfListoff);    //离线的放下面
            gmap.put("list", gfList);
            gmapList.add(gmap);
        }

        Map<String, Object> nogmap = new LinkedHashMap<String, Object>(); //存放无分组的好友(无分组就是默认分组)
        nogmap.put("groupname", "未分组");
        nogmap.put("id", "9999");
        nogmap.put("online", 999);
        List<Object> nogfList = new ArrayList<Object>();
        List<Object> nogfListon = new ArrayList<Object>();                //存放在线的
        List<Object> nogfListoff = new ArrayList<Object>();                //存放离线线的
        for (int n = 0; n < noGList.size(); n++) {
            Map<String, Object> nfpd = new LinkedHashMap<String, Object>();
            nfpd.put("username", noGList.get(n).getString("NAME"));        //姓名
            nfpd.put("id", noGList.get(n).getString("FUSERNAME"));        //用户名
            nfpd.put("avatar", null == noGList.get(n).get("PHOTO2") ? "assets/images/user/avatar-2.jpg" : noGList.get(n).getString("PHOTO2"));        //头像
            nfpd.put("sign", null == noGList.get(n).get("AUTOGRAPH") ? "" : noGList.get(n).getString("AUTOGRAPH"));                                //个性签名
            if (null != noGList.get(n).get("ONLINE") && "hide".equals(noGList.get(n).getString("ONLINE"))) {//好友在线状态设置为离线的(不管是否真实在线,都判定离线)
                nfpd.put("status", "offline");                                //离线状态
                nogfListoff.add(nfpd);
            } else {                                                            //如果设置的状态为在线，再去判断真实是否在线
                Collection<String> user = ChatServerPool.getOnlineUser();    //所有在线用户
                if (!user.contains(noGList.get(n).getString("FUSERNAME"))) {
                    nfpd.put("status", "offline");                            //离线状态
                    nogfListoff.add(nfpd);
                } else {                                                        //其它情况都是在线状态，在线状态就不用加status了
                    nogfListon.add(nfpd);
                }
            }
        }
        nogfList.addAll(nogfListon);    //在线的放上面
        nogfList.addAll(nogfListoff);    //离线的放下面
        if (noGList.size() > 0) {
            nogmap.put("list", nogfList);
            gmapList.add(nogmap);
        }

        nmap.put("friend", gmapList); //好友分组及好友数据

        List<Object> qfList = new ArrayList<Object>();
        List<PageData> myqList = getMyAllQgroup(pd);                    //我在的全部群列表
        for (int i = 0; i < myqList.size(); i++) {
            Map<String, Object> qmap = new LinkedHashMap<String, Object>();
            qmap.put("groupname", myqList.get(i).getString("NAME"));    //群名称
            qmap.put("id", myqList.get(i).getString("QGROUP_ID"));        //群ID
            qmap.put("avatar", myqList.get(i).getString("PHOTO"));        //群头像
            qfList.add(qmap);
        }
        nmap.put("group", qfList); //群组数据
        return nmap;
    }

    /**
     * 我在的全部群列表
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> getMyAllQgroup(PageData pd) throws Exception {
        pd.put("USERNAME", getUsername());        //当前用户
        PageData ipd = new PageData();
        ipd = iQgroupService.findById(pd);
        if (null == ipd) {                                    //当我没有任何群时添加数据，否则修改
            pd.put("item", "('null')");
        } else {
            pd.put("item", ipd.getString("qgroups") + "'fh')");
        }
        return qgroupService.mylistAll(pd);                    //列出Qgroup列表
    }

    /**
     * 获取群成员接口
     *
     * @return
     */
    @RequestMapping(value = "/getMembers")
    @ResponseBody
    public Object getMembers() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Map<String, Object> dmap = new LinkedHashMap<String, Object>();
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());
        pd.put("QGROUP_ID", pd.getString("id"));                        //群组的id
        try {
            List<Object> qlist = new ArrayList<Object>();                //存放群成员
            List<PageData> varList = iQgroupService.listAll(pd);        //全部群成员
            int n = 0;
            for (int i = 0; i < varList.size(); i++) {
                Map<String, Object> umap = new LinkedHashMap<String, Object>();
                umap.put("username", varList.get(i).getString("name"));    //姓名
                umap.put("id", varList.get(i).getString("username"));    //用户名即id
                umap.put("avatar", null == varList.get(i).get("photo2") ? "assets/images/user/avatar-2.jpg" : http_root_path + varList.get(i).getString("photo2"));    //头像
                umap.put("sign", varList.get(i).getString("autograph"));//个性签名
                qlist.add(umap);
                n++;
            }
            dmap.put("members", n);
            dmap.put("list", qlist);
            map.put("code", 0);
            map.put("msg", "");
            map.put("data", dmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 聊天上传图片接口
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateImg")
    @ResponseBody
    public Object updateImg(@RequestParam(required = false) MultipartFile file) throws Exception {
        Map<String, Object> zmap = new LinkedHashMap<String, Object>();
        Map<String, Object> nmap = new LinkedHashMap<String, Object>();
        String ffile = DateUtil.getDays(), fileName = "";
        if (null != file && !file.isEmpty()) {
            String filePath = PathUtil.getProjectpath() + Const.FILEPATHIMG + ffile;        //图片上传路径
            fileName = FileUpload.fileUp(file, filePath, UuidUtil.get32UUID());                    //执行上传
            nmap.put("src", Const.FILEPATHIMG + ffile + "/" + fileName);
            zmap.put("code", 0);            //0表示成功，其它表示失败
            zmap.put("msg", "");            //失败信息
            zmap.put("data", nmap);
        } else {
            nmap.put("src", "");
            zmap.put("code", 1);            //0表示成功，其它表示失败
            zmap.put("msg", "上传失败");        //失败信息
            zmap.put("data", nmap);
        }
        return zmap;
    }

    /**
     * 聊天上传文件接口
     *
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updateFile")
    @ResponseBody
    public Object updateFile(@RequestParam(required = false) MultipartFile file) throws Exception {
        Map<String, Object> zmap = new LinkedHashMap<String, Object>();
        Map<String, Object> nmap = new LinkedHashMap<String, Object>();
        String ffile = DateUtil.getDays(), fileName = "";
        if (null != file && !file.isEmpty()) {
            String filePath = PathUtil.getProjectpath() + Const.FILEPATHFILE + ffile;        //文件上传路径
            fileName = FileUpload.fileUp(file, filePath, UuidUtil.get32UUID());                    //执行上传
            nmap.put("src", Const.FILEPATHFILE + ffile + "/" + fileName);
            nmap.put("name", fileName);
            zmap.put("code", 0);            //0表示成功，其它表示失败
            zmap.put("msg", "");            //失败信息
            zmap.put("data", nmap);
        } else {
            nmap.put("src", "");
            nmap.put("name", "");
            zmap.put("code", 1);            //0表示成功，其它表示失败
            zmap.put("msg", "上传失败");        //失败信息
            zmap.put("data", nmap);
        }
        return zmap;
    }

    /**
     * 修改状态(在线隐身、个性签名、皮肤)接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/editState")
    @ResponseBody
    public Object editState() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());
        pd.put(Const.SESSION_USERNAME, getUsername());        //当前用户名
        if (null != imstateService.findByUsername(pd)) {                    //判断数据库有没有此用的数据，有则修改，无则新增
            String TYPE = pd.getString("TYPE");
            if ("online".equals(TYPE)) {
                imstateService.editOnline(pd);    //修改在线状态
            } else if ("auto".equals(TYPE)) {
                imstateService.editAuto(pd);    //修改个性签名
            } else {
                imstateService.editSign(pd);    //修改皮肤
            }
        } else {
            pd.put("IMSTATE_ID", UuidUtil.get32UUID());                                                                    //主键
            pd.put("ONLINE", null != pd.get("ONLINE") && "hide".equals(pd.getString("ONLINE")) ? "hide" : "online");        //在线or隐身
            pd.put("AUTOGRAPH", null != pd.get("AUTOGRAPH") ? pd.getString("AUTOGRAPH") : "I LOVE FH Admin");                //个性签名
            pd.put("SIGN", null != pd.get("SIGN") ? pd.getString("SIGN") : "5.jpg");                                        //皮肤
            imstateService.save(pd);
        }
        return map;
    }

    /**
     * 获取皮肤接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getImsign")
    @ResponseBody
    public Object getImsign() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        PageData pd = new PageData();
        pd.put(Const.SESSION_USERNAME, getUsername());        //当前用户名
        pd = imstateService.findByUsername(pd);
        if (null != pd) {
            map.put("imsign", pd.getString("sign"));
        } else {
            map.put("imsign", "5.jpg");
        }
        return map;
    }

    /**
     * 添加好友接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addFriends")
    @ResponseBody
    public Object addFriends() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String result = "";
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());
        //不能添加自身
        if (pd.getString("FUSERNAME").equals(pd.put("USERNAME", getUsername()))) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", -1);
            resultMap.put("data", null);
            resultMap.put("msg", "不能添加自己");
            return resultMap;
        }
        pd.put("USERNAME", getUsername());        //用户名
        if (null != friendsService.findMyFriend(pd)) {        //判断是否已经是好友了
            result = "00";
        } else {
            pd.put("FRIENDS_ID", UuidUtil.get32UUID());            //主键
            pd.put("CTIME", DateUtil.date2Str(new Date()));    //申请时间
            pd.put("ALLOW", "yes");                        //是否允许
            pd.put("FGROUP_ID", "9999".equals(pd.getString("FGROUP_ID")) ? "" : pd.getString("FGROUP_ID"));    //分组, 9999 代表无分组
            pd.put("DTIME", "");                            //处理时间，刚添加等待对方处理，现在为空，处理的时候录入时间
            friendsService.save(pd);
            /*存入IM系统消息表中IM_SYSMSG*/
		/*	pd.put("SYSMSG_ID", UuidUtil.get32UUID());				//主键
			pd.put("USERNAME", pd.getString("FUSERNAME"));		//接收者用户名
			pd.put("FROMUSERNAME", getUsername());	//发送者用户名(即当前用户)
			pd.put("CTIME", DateUtil.date2Str(new Date()));		//操作时间
			pd.put("REMARK", pd.getString("BZ"));				//留言
			pd.put("TYPE", "friend");							//类型
			pd.put("CONTENT", "申请添加你为好友");				//事件内容
			pd.put("ISDONE", "no");								//是否完成
			pd.put("DTIME", "");								//完成时间
			pd.put("QGROUP_ID", "");							//申请加群是有值
			pd.put("DREAD", "0");								//阅读状态 0 未读
			sysmsgService.save(pd);*/
            result = "01";
        }
        map.put("result", result);
        return map;
    }

    /**
     * 添加群接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addQGroup")
    @ResponseBody
    public Object addQGroup() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String result = "";
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());
        pd.put("USERNAME", getUsername());        //用户名
        if (null != iQgroupService.findByIdandQid(pd)) {        //判断是否在此群了
            result = "00";
        } else {
            String FUSERNAME = qgroupService.findById(pd).getString("USERNAME");//通过群ID获取群主用户名
            String QNAME = qgroupService.findById(pd).getString("NAME");        //群名
            /*存入IM系统消息表中IM_SYSMSG*/
            pd.put("SYSMSG_ID", UuidUtil.get32UUID());                //主键
            pd.put("USERNAME", FUSERNAME);                        //接收者用户名
            pd.put("FROMUSERNAME", getUsername());    //发送者用户名(即当前用户)
            pd.put("CTIME", DateUtil.date2Str(new Date()));        //操作时间
            pd.put("REMARK", pd.getString("BZ"));                //留言
            pd.put("TYPE", "group");                            //类型
            pd.put("CONTENT", "申请加入群:" + QNAME);                //事件内容
            pd.put("ISDONE", "no");                                //是否完成
            pd.put("DTIME", "");                                //完成时间
            pd.put("DREAD", "0");                                //阅读状态 0 未读
            sysmsgService.save(pd);
            result = "01";
        }
        map.put("result", result);
        return map;
    }

    /**
     * 打开消息盒子页面(好友申请，群申请消息)
     *
     * @param
     */
    @RequestMapping(value = "/msgbox")
    public String msgbox() {
        return "fhim/msgbox";
    }

    /**
     * 消息盒子数据接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/msgboxdata")
    @ResponseBody
    public Object msgboxdata() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        PageData pd = new PageData();
        List<Object> zmsglist = new ArrayList<Object>();
        pd.put("USERNAME", getUsername());            //当前用户名
        List<PageData> msglist = sysmsgService.listAll(pd);
        for (int i = 0; i < msglist.size(); i++) {
            Map<String, Object> msgpd = new LinkedHashMap<String, Object>();
            msgpd.put("id", msglist.get(i).getString("sysmsgId"));
            if ("no".equals(msglist.get(i).getString("isdone"))) {                //消息没办理(新消息)
                msgpd.put("content", msglist.get(i).getString("content"));        //消息内容
                msgpd.put("uid", msglist.get(i).getString("username"));            //接收者用户名
                msgpd.put("from", msglist.get(i).getString("fromusername"));    //消息发送者
                msgpd.put("from_group", 0);
                msgpd.put("type", msglist.get(i).getString("type"));
                msgpd.put("remark", msglist.get(i).getString("remark"));        //留言
                msgpd.put("href", null);
                msgpd.put("read", 1);
                msgpd.put("time", msglist.get(i).getString("ctime"));            //申请时间
                msgpd.put("qgroupid", msglist.get(i).getString("qgroupId"));    //申请添加群时有值
                Map<String, Object> statepd = new LinkedHashMap<String, Object>();
                statepd.put("id", msglist.get(i).getString("fromusername"));    //用户名
                statepd.put("avatar", null == msglist.get(i).get("photo2") ? "assets/images/user/avatar-2.jpg" : msglist.get(i).getString("photo2"));    //头像
                statepd.put("username", msglist.get(i).getString("name"));        //姓名
                statepd.put("sign", msglist.get(i).getString("autograph"));        //签名
                msgpd.put("user", statepd);
            } else {
                msgpd.put("content", msglist.get(i).getString("content"));        //消息内容
                msgpd.put("uid", msglist.get(i).getString("username"));            //接收者用户名
                msgpd.put("from", null);
                msgpd.put("from_group", null);
                msgpd.put("type", 1);
                msgpd.put("remark", null);
                msgpd.put("href", null);
                msgpd.put("read", 1);
                msgpd.put("time", msglist.get(i).getString("dtime"));            //处理时间
                Map<String, Object> statepd = new LinkedHashMap<String, Object>();
                statepd.put("id", null);
                msgpd.put("user", statepd);
            }
            zmsglist.add(msgpd);
        }
        map.put("code", 0);
        map.put("pages", 1);
        map.put("data", zmsglist);
        return map;
    }

    /**
     * 消息盒子未读消息总数接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getMsgCount")
    @ResponseBody
    public Object getMsgCount() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String result = "";
        PageData pd = new PageData();
        pd.put("USERNAME", getUsername());                //用户名
        pd = sysmsgService.getMsgCount(pd);
        int count = Integer.parseInt(pd.get("msgCount").toString());
        if (count > 0) {        //未读消息数大于0
            result = "01";
        } else {
            result = "00";
        }
        map.put("count", count);
        map.put("result", result);
        return map;
    }


    /**
     * 同意申请(好友或者群)接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/agree")
    @ResponseBody
    public Object agree() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());

        if ("friend".equals(pd.getString("TYPE"))) {            //同意好友申请
            pd.put("USERNAME", getUsername());    //当前用户名

            //修改消息状态
            pd.put("CONTENT", "您同意了" + pd.getString("FNAME") + "申请好友并加对方好友");
            pd.put("DTIME", DateUtil.date2Str(new Date()));    //完成时间
            pd.put("ISDONE", "yes");
            sysmsgService.edit(pd);

            pd.put("USERNAME", getUsername());    //用户名
            pd.put("ALLOW", "yes");                            //是否允许
            pd.put("DTIME", DateUtil.date2Str(new Date()));    //处理时间，刚添加等待对方处理，现在为空，处理的时候录入时间

            if (null == friendsService.findMyFriend(pd)) {
                //添加对方好友
                pd.put("FRIENDS_ID", UuidUtil.get32UUID());            //主键
                pd.put("CTIME", DateUtil.date2Str(new Date()));    //申请时间
                pd.put("FGROUP_ID", "9999".equals(pd.getString("FGROUP_ID")) ? "" : pd.getString("FGROUP_ID"));    //分组, 9999 代表无分组
                pd.put("BZ", "同意对方申请好友并加对方好友");
                friendsService.save(pd);
            }
            //修改对方好友(本人)的状态为同意yes
            friendsService.editAllow(pd);

            //添加系统消息
            pd.put("SYSMSG_ID", UuidUtil.get32UUID());                                //主键
            pd.put("USERNAME", pd.getString("FUSERNAME"));                        //接收者用户名
            pd.put("FROMUSERNAME", getUsername());                    //发送者用户名(即当前用户)
            pd.put("CTIME", DateUtil.date2Str(new Date()));                        //操作时间
            pd.put("REMARK", "");                                                //留言
            pd.put("TYPE", "friend");                                            //类型
            pd.put("CONTENT", getName() + " 已经同意你的好友申请");    //事件内容
            pd.put("QGROUP_ID", "");                                            //申请加群是有值
            pd.put("DREAD", "0");                                                //阅读状态 0 未读
            sysmsgService.save(pd);
        } else {    //同意群申请

            PageData msgpd = new PageData();
            msgpd = sysmsgService.findById(pd);

            //修改消息状态
            pd.put("CONTENT", "您同意了" + pd.getString("FNAME") + msgpd.getString("CONTENT"));
            pd.put("DTIME", DateUtil.date2Str(new Date()));                        //完成时间
            pd.put("ISDONE", "yes");
            sysmsgService.edit(pd);

            pd.put("USERNAME", pd.getString("FUSERNAME"));
            if (null == iQgroupService.findByIdandQid(pd)) {        //判断是否在此群了

                //添加系统消息
                pd.put("SYSMSG_ID", UuidUtil.get32UUID());                                //主键
                pd.put("FROMUSERNAME", getUsername());                    //发送者用户名(即当前用户)
                pd.put("CTIME", DateUtil.date2Str(new Date()));                        //操作时间
                pd.put("REMARK", "");                                                //留言
                pd.put("TYPE", "group");                                            //类型
                pd.put("CONTENT", getName() + " 已经同意你" + msgpd.getString("CONTENT"));    //事件内容
                pd.put("DREAD", "0");                                                //阅读状态 0 未读
                sysmsgService.save(pd);

                //把此用户加入到群中
                PageData ipd = new PageData();
                ipd = iQgroupService.findById(pd);
                if (null == ipd) {                                //当我没有任何群时添加数据，否则修改
                    pd.put("QGROUPS", "('" + pd.getString("QGROUP_ID") + "',");
                    pd.put("IQGROUP_ID", UuidUtil.get32UUID());        //主键
                    iQgroupService.save(pd);
                } else {
                    pd.put("QGROUPS", ipd.getString("QGROUPS").replaceAll("'" + pd.getString("QGROUP_ID") + "',", "") + "'" + pd.getString("QGROUP_ID") + "',");//防止同一群，重复的添加
                    iQgroupService.edit(pd);
                }
            }
        }
        map.put("result", "success");
        return map;
    }

    /**
     * 拒绝申请(好友或者群)接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/refuse")
    @ResponseBody
    public Object refuse() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String result = "";
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());

        if ("friend".equals(pd.getString("TYPE"))) {            //同意好友申请
            pd.put("USERNAME", getUsername());    //当前用户名

            //修改消息状态
            pd.put("CONTENT", "您拒绝了" + pd.getString("FNAME") + "申请好友");
            pd.put("DTIME", DateUtil.date2Str(new Date()));    //完成时间
            pd.put("ISDONE", "yes");
            sysmsgService.edit(pd);

            //从对方好友列表中删除本人
            friendsService.pullblack(pd);

            //添加系统消息
            pd.put("SYSMSG_ID", UuidUtil.get32UUID());                                //主键
            pd.put("USERNAME", pd.getString("FUSERNAME"));                        //接收者用户名
            pd.put("FROMUSERNAME", getUsername());                    //发送者用户名(即当前用户)
            pd.put("CTIME", DateUtil.date2Str(new Date()));                        //操作时间
            pd.put("REMARK", "");                                                //留言
            pd.put("TYPE", "friend");                                            //类型
            pd.put("CONTENT", getName() + " 拒绝了你的好友申请");        //事件内容
            pd.put("QGROUP_ID", "");                                            //申请加群是有值
            pd.put("DREAD", "0");                                                //阅读状态 0 未读
            sysmsgService.save(pd);
        } else {    //拒绝群申请

            PageData msgpd = new PageData();
            msgpd = sysmsgService.findById(pd);

            pd.put("USERNAME", pd.getString("FUSERNAME"));
            if (null == iQgroupService.findByIdandQid(pd)) {        //判断是否在此群了

                //修改消息状态
                pd.put("CONTENT", "您拒绝了 " + pd.getString("FNAME") + msgpd.getString("CONTENT"));
                pd.put("DTIME", DateUtil.date2Str(new Date()));                        //完成时间
                pd.put("ISDONE", "yes");
                sysmsgService.edit(pd);

                //添加系统消息
                pd.put("SYSMSG_ID", UuidUtil.get32UUID());                                //主键
                pd.put("FROMUSERNAME", getUsername());                    //发送者用户名(即当前用户)
                pd.put("CTIME", DateUtil.date2Str(new Date()));                        //操作时间
                pd.put("REMARK", "");                                                //留言
                pd.put("TYPE", "group");                                            //类型
                pd.put("CONTENT", getName() + " 已经拒绝你" + msgpd.getString("CONTENT"));    //事件内容
                pd.put("DREAD", "0");                                                //阅读状态 0 未读
                sysmsgService.save(pd);

            } else {//已经在此群了，就不能拒绝了，修改消息状态为同意

                //修改消息状态
                pd.put("CONTENT", "同意了" + pd.getString("FNAME") + msgpd.getString("CONTENT"));
                pd.put("DTIME", DateUtil.date2Str(new Date()));                        //完成时间
                pd.put("ISDONE", "yes");
                sysmsgService.edit(pd);
                result = "01";
            }
        }
        map.put("result", result);
        return map;
    }

    /**
     * 消息设置成已读
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/read")
    @ResponseBody
    public Object read() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        PageData pd = new PageData();
        pd.put("USERNAME", getUsername());        //用户名
        sysmsgService.read(pd);
        map.put("result", "success");
        return map;
    }

    /**
     * 获取未读消息(离线消息)接口
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getNoreadMsg")
    @ResponseBody
    public Object getNoreadMsg() throws Exception {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        String result = "";
        PageData pd = new PageData();
        pd = new PageData(this.getRequest());
        pd.put("USERNAME", getUsername());        //用户名
        List<PageData> varList = hismsgService.listAllnoread(pd);
        if (varList.size() > 0) {
            result = "has";
            map.put("list", varList);
            hismsgService.edit(pd);                            //设置成已读状态
        } else {
            result = "no";
        }
        map.put("result", result);
        return map;
    }

}
