package com.gruzini.messenger.controllers;

import com.gruzini.messenger.dto.PresenceEventDto;
import com.gruzini.messenger.models.LogoService;
import com.gruzini.messenger.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PresenceController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final LogoService logoService;

    public User publishLoginInfo(User newUser) throws InterruptedException {
        simpMessagingTemplate.convertAndSend("/topic/allLogins", new PresenceEventDto(newUser.getUsername(),
                newUser.getColorCode(),
                newUser.getLogoLink(),
                PresenceEventDto.PresenceEventType.LOGGED_IN));
        return newUser;
    }

    public User publishLogOutInfo(User newUser) throws InterruptedException {
        simpMessagingTemplate.convertAndSend("/topic/allLogins", new PresenceEventDto(newUser.getUsername(),
                newUser.getColorCode(),
                newUser.getLogoLink(),
                PresenceEventDto.PresenceEventType.LOGGED_OUT));
        return newUser;
    }
}
