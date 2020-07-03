package com.gruzini.messenger.controllers;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/publishMessage")
    @SendTo("/topic/allMessages")
    public Message publishMessage(SendMessageDto messageDto) throws InterruptedException {
        System.out.println("messageDto = " + messageDto);
        return messageService.save(messageDto);
    }
}
