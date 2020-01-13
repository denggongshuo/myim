package com.pit.myim.config;

import com.pit.myim.plugins.websocketInstantMsg.ChatServer;
import com.pit.myim.plugins.websocketOnline.OnlineChatServer;
import com.pit.myim.util.Const;
import com.pit.myim.util.IniFileUtil;
import com.pit.myim.util.PathUtil;
import org.java_websocket.WebSocketImpl;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 说明：web服务器启动后立即执行
 * 作者：FH Admin Q313596790
 */
@Component
@Order(value = 1) // 1 代表启动顺序
public class StartWebsocketServer implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        startWebsocketOnline();		//启动在线管理服务
        startWebsocketInstantMsg();	//启动即时聊天服务
        System.out.println("--------------系统启动成功--------------");
    }

    /**
     * 启动在线管理服务
     */
    public void startWebsocketOnline(){
        WebSocketImpl.DEBUG = false;
        OnlineChatServer s;
        try {
            //String infFilePath = PathUtil.getClasspath()+Const.SYSSET;
            String infFilePath=java.net.URLDecoder.decode(PathUtil.getClasspath()+ Const.SYSSET,"utf-8");//配置文件路径
            String onlinePort = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "onlinePort", "8869");			//在线管理端口
            s = new OnlineChatServer(Integer.parseInt(onlinePort));
            s.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动即时聊天服务
     */
    public void startWebsocketInstantMsg(){
        WebSocketImpl.DEBUG = false;
        ChatServer s;
        try {
            //String infFilePath = PathUtil.getClasspath()+ Const.SYSSET;
            String infFilePath=java.net.URLDecoder.decode(PathUtil.getClasspath()+ Const.SYSSET,"utf-8");//配置文件路径
            String imPort = IniFileUtil.readCfgValue(infFilePath, "SysSet1", "imPort", "8879");					//即时端口
            s = new ChatServer(Integer.parseInt(imPort));
            s.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
