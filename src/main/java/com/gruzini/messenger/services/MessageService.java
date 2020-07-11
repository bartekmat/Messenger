package com.gruzini.messenger.services;

import com.gruzini.messenger.controllers.PrivateOutboundMessageController;
import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.dto.SendPrivateMessageDto;
import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.models.MessageType;
import com.gruzini.messenger.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class MessageService {
    private final List<Message> allMessages = new ArrayList<>();
    private final PresenceService presenceService;
    private final PrivateOutboundMessageController privateOutboundMessageController;
    private final GifService gifService;

    public MessageService(PresenceService presenceService, PrivateOutboundMessageController privateOutboundMessageController, GifService gifService) {
        this.presenceService = presenceService;
        this.privateOutboundMessageController = privateOutboundMessageController;
        this.gifService = gifService;
    }

    public Message postPublicMessage(final SendMessageDto messageDto, final String principalName) {
        final Message message = prepareMessage(messageDto, principalName);
        allMessages.add(message);
        return message;
    }

    public List<Message> readAllPublicMessages() {
        return allMessages;
    }

    public void postPrivateMessage(final SendPrivateMessageDto messageDto, final String principalName) {
        final Message message = prepareMessage(messageDto, principalName);
        privateOutboundMessageController.publishPrivateMessage(message);
    }

    private Message prepareMessage(final SendMessageDto messageDto, final String senderPrincipalName) {
        final User sender = presenceService.getUser(senderPrincipalName);
        final User recipient = (messageDto instanceof SendPrivateMessageDto)
                ? presenceService.getUserByName(((SendPrivateMessageDto) messageDto).getRecipient())
                : null;
        log.info("Received message " + messageDto.getContent() + " from " + sender.getUsername() + " to " + (recipient == null ? "all" : recipient.getUsername()));

        String content = messageDto.getContent();
        MessageType messageType = MessageType.TEXT;

        if (gifService.isGifMessage(content)){
            content = gifService.prepareGifMessageText(content);
            messageType = MessageType.GIF;
        }

        return Message.builder()
                .content(content)
                .sender(sender)
                .recipient(recipient)
                .createdAt(LocalDateTime.now())
                .type(messageType)
                .build();
    }

}
