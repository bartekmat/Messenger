package com.gruzini.messenger.services;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.models.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageService {
    private final List<Message> allMessages = new ArrayList<>();
    private final PresenceService presenceService;

    public MessageService(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    public Message postPublicMessage(final SendMessageDto dto, final String sessionId) {
        final Message message = new Message(presenceService.getUser(sessionId), dto.getContent(), LocalDateTime.now());
        allMessages.add(message);
        return message;
    }

    public Message save(SendMessageDto messageDto, String sessionId) throws InterruptedException {
        postPublicMessage(messageDto, sessionId);
        Thread.sleep(1000);
        final List<Message> messages = getAllMessages();
        System.out.println(messages.get(messages.size() - 1));
        return messages.get(messages.size() - 1);
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }
}
