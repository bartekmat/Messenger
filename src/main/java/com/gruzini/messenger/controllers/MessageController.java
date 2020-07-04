package com.gruzini.messenger.controllers;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.models.User;
import com.gruzini.messenger.services.MessageService;
import org.hibernate.Session;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/publishMessage")
    @SendTo("/topic/allMessages")
    public Message publishMessage(SendMessageDto messageDto, MessageHeaderAccessor accessor){
        final String sessionId = ((StompHeaderAccessor) accessor).getSessionId();
        return messageService.postPublicMessage(messageDto, sessionId);
    }

}
