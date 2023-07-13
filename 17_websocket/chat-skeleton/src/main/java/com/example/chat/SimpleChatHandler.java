package com.example.chat;

import com.example.chat.dto.ChatMessage;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SimpleChatHandler extends TextWebSocketHandler {
    // 현재 연결되어 있는 클라이언트를 관리하기 위한 리스트
    private final List<WebSocketSession> sessions = new ArrayList<>();

    // 사용자 이름으로 세션을 구분
    private Map<String, WebSocketSession> sessionByUsername;

    public void broadcast(String message) throws IOException {
        for (WebSocketSession connected : sessions) {
            connected.sendMessage(new TextMessage(message));
        }
    }

    @Override
    // Websocket 최초 연결 시 실행
    public void afterConnectionEstablished(WebSocketSession session) throws Exception { // session: 연결된 클라이언트를 나타내는 객체
        // 방금 참여한 사용자를 저장
        sessions.add(session);
        log.info("connected with session id: {}, total sessions: {}", session.getId(), sessions.size());
    }

    @Override
    // WebSocket 메시지를 받으면 실행
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        ChatMessage chatMessage = new Gson().fromJson(payload, ChatMessage.class);
        log.info("received: {}", payload);
        for (WebSocketSession connected : sessions) {
            connected.sendMessage(message);
        }
    }

    @Override
    // WebSocket 연결이 종료되었을 때
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("connection with {} closed", session.getId());
        // 더 이상 세션 객체를 보유하지 않도록
        sessions.remove(session);
        super.afterConnectionClosed(session, status);
    }
}
