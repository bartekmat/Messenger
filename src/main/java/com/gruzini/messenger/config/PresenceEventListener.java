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
    public void handleSessionConnected(SessionConnectEvent event) throws InterruptedException {
        final SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        /*
        for anonymous it is sessionId - we create it in UserInterceptor
        for logged with github - it is login - parsed from oauth json
         */
        final String principalName = event.getUser().getName();
        log.info("Connected! user : " + principalName);
        presenceService.userLoggedIn(event.getUser());
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) throws InterruptedException {
        final String principalName = event.getUser().getName();
        log.info("Disconnected! user : " + principalName);
        presenceService.userLoggedOut(principalName);
    }
}
