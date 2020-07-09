package com.gruzini.messenger.controllers;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.services.MessageService;
import com.gruzini.messenger.services.PresenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Slf4j
public class FrontendController {

    private final MessageService messageService;
    private final PresenceService presenceService;

    public FrontendController(MessageService messageService, PresenceService presenceService) {
        this.messageService = messageService;
        this.presenceService = presenceService;
    }
    @GetMapping("/")
    public String index(final Model model, Principal principal){
        model.addAttribute("principalName", principal == null ? "anonymous" : principal.getName());
        return "index";
    }

    @GetMapping("/chat")
    public String showChat(final Model model) {
        model.addAttribute("newMessage", new SendMessageDto());
        model.addAttribute("allMessages", messageService.getAllMessages());
        model.addAttribute("activeUsers", presenceService.getAllActiveUsers());
        return "chat";
    }
}
