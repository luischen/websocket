package com.manu.icare_main.foundation.websocket;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/websocket")
@Component
public class WebsocketServer{
	//统计在线人数
    private static int onlineCount = 0;
    //用本地线程保存session
    private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();
    //保存所有连接上的session
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }
    public static synchronized void addOnlineCount() {
        onlineCount++;
    }
    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    //连接
    @OnOpen
    public void onOpen(Session session) {
    	//TODO 鉴权处理
    	List<String> tokenList = session.getRequestParameterMap().get("token");
    	if(tokenList == null || tokenList.isEmpty() ||  !"test".equals(tokenList.get(0))) {
    		sendMsg(session, "连接服务器失败，鉴权失败！");
    		try {
				session.close();
			} catch (IOException e) {
				
			}
    		return;
    	}
        sessions.set(session);
        addOnlineCount();
        sessionMap.put(session.getId(), session);
        System.out.println("【" + session.getId() + "】连接上服务器======当前在线人数【" + getOnlineCount() + "】");
        //连接上后给客户端一个消息
        sendMsg(session, "连接服务器成功！");
    }

    //关闭
    @OnClose
    public void onClose(Session session) {
    	if(sessionMap.containsKey(session.getId())){
    		subOnlineCount();
            sessionMap.remove(session.getId());
    	}        
        System.out.println("【" + session.getId() + "】退出了连接======当前在线人数【" + getOnlineCount() + "】");
    }

    //接收消息   客户端发送过来的消息
    @OnMessage
    public void onMessage(String message, Session session) {
    	System.out.println("【" + session.getId() + "】客户端的发送消息======内容【" + message + "】");
    	if(sessionMap.containsKey(session.getId())){
    		 String messageSend = "【服务器】接收到【" + session.getId() + "】客户端的发送消息======内容【" + message + "】";
    	     broadcast(messageSend);
    	}else {
    		System.out.println("由于【"+ session.getId() + "】未通过验证，拒绝接收消息");
    	}
        
       
    }

    //异常
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生异常!");
        throwable.printStackTrace();
    }



    //统一的发送消息方法
    public synchronized void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void broadcast(String msg) {
    	for (Session s : sessionMap.values()) {
    		sendMsg(s, msg);
        }
    }
}
