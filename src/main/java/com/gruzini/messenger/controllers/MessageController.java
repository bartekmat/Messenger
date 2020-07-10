package com.gruzini.messenger.controllers;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.dto.SendPrivateMessageDto;
import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/publishPublicMessage")
    @SendTo("/topic/allMessages")
    public Message publishMessage(@Payload SendMessageDto messageDto, Principal principal) {
        System.out.println("principal = " + principal.getName());
        return messageService.postPublicMessage(messageDto, principal.getName());
    }

    @MessageMapping("/publishPrivateMessage")
    public void publishPrivateMessage(@Payload SendPrivateMessageDto messageDto,
                                      Principal principal) {
        messageService.postPrivateMessage(messageDto, principal.getName());
    }

}
