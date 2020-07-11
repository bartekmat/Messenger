package com.gruzini.messenger.controllers;

import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.models.User;
import com.gruzini.messenger.services.MessageService;
import com.gruzini.messenger.services.PresenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class RecapRestController {
    private final PresenceService presenceService;
    private final MessageService messageService;

    public RecapRestController(PresenceService presenceService, MessageService messageService) {
        this.presenceService = presenceService;
        this.messageService = messageService;
    }

    @GetMapping(path = "/allActiveUsers")
    public Collection<User> fetchAllActiveUsers() {
        return presenceService.getAllActiveUsers();
    }
    @GetMapping(path = "/allPublicMessages")
    public Collection<Message> fetchAllPublicMessages() {
        return messageService.readAllPublicMessages();
    }
}
