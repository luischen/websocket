package com.manu.icare_main.foundation.websocket;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * websocket 定时接口，主要用于测试通过服务端直接发起通知的功能
 * */
@Component
@Configurable
@EnableScheduling
public class WebsocketPush {
	

	@Autowired
	private WebsocketServer server;
	
	@Scheduled(cron = "0/30 * * * * ? ")
	public void push() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		server.broadcast("服务器时间:"+sdf.format(new Date()));
	}
}
