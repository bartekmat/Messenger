package com.gruzini.messenger.services;

import com.gruzini.messenger.dto.SendMessageDto;
import com.gruzini.messenger.models.Message;
import com.gruzini.messenger.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MessageService {
    private final List<Message> allMessages = new ArrayList<>();
    private final UsernameService usernameService;

    public MessageService(UsernameService usernameService) {
        this.usernameService = usernameService;
    }

    public void postPublicMessage(final SendMessageDto dto){
        allMessages.add(new Message(new User("cat"), dto.getContent(), LocalDateTime.now()));
    }
    public Message save(SendMessageDto messageDto) throws InterruptedException {
        System.out.println("messageDto = " + messageDto.getContent());
        postPublicMessage(messageDto);
        Thread.sleep(1000);
        final List<Message> messages = getAllMessages();
        System.out.println(messages.get(messages.size()-1));
        return messages.get(messages.size()-1);
    }

    public List<Message> getAllMessages() {
        return allMessages;
    }
}
