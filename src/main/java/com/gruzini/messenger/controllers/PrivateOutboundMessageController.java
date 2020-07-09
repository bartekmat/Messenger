package com.gruzini.messenger.controllers;

import com.gruzini.messenger.models.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class PrivateOutboundMessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public PrivateOutboundMessageController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void publishPrivateMessage(Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getSender().getUsername(), "/topic/privateMessages", message);
        simpMessagingTemplate.convertAndSendToUser(message.getRecipient().getUsername(), "/topic/privateMessages", message);
    }
}
