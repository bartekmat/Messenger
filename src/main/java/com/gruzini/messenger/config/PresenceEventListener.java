package com.gruzini.messenger.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Component
@Slf4j
public class PresenceEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        final String sessionId = headers.getSessionId();
        log.info("Connected! user with sessionId : " + sessionId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        final String sessionId = headers.getSessionId();
        log.info("Disconnected! user with sessionId : " + sessionId);
    }
}
