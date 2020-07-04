package com.gruzini.messenger.config;

import com.gruzini.messenger.services.PresenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class PresenceEventListener {

    private final PresenceService presenceService;

    public PresenceEventListener(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) throws InterruptedException {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        final String sessionId = headers.getSessionId();
        log.info("Connected! user with sessionId : " + sessionId);
        presenceService.userLoggedIn(sessionId);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        final String sessionId = headers.getSessionId();
        log.info("Disconnected! user with sessionId : " + sessionId);
        presenceService.userLoggedOut(sessionId);
    }
}
