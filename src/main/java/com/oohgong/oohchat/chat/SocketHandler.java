package com.oohgong.oohchat.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {
	
	private static List<WebSocketSession> weblist = new ArrayList<>();
	HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
	
	//메세지를 보냈을 때
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String msg = message.getPayload();
		
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			
			try {
				log.info("socketHandler - msg : "+msg);
				wss.sendMessage(new TextMessage(msg));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//소켓에 연결되었을 때
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String uri = session.getUri().toString();
		uri = uri.substring(uri.lastIndexOf("/")+1);
		
		String message = "{\"type\":\"connect\",\"roomNum\":\""+uri+"\"}";
		sessionMap.put(session.getId(), session);
		
		for (String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			try {
				wss.sendMessage(new TextMessage(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//연결이 끊겼을 때
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		String uri = session.getUri().toString();
		uri = uri.substring(uri.lastIndexOf("/")+1);
		
		String message = "{\"type\":\"disconnect\",\"roomNum\":\""+uri+"\"}";
		sessionMap.remove(session.getId());
		
		for(String key : sessionMap.keySet()) {
			WebSocketSession wss = sessionMap.get(key);
			
			try {
				wss.sendMessage(new TextMessage(message));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
