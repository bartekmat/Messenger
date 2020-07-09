package com.gruzini.messenger.services;

import com.gruzini.messenger.controllers.PresenceController;
import com.gruzini.messenger.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {
    private Map<String, User> activeUsers = new HashMap<>();
    private final UsernameService usernameService;
    private final PresenceController presenceController;

    public void userLoggedIn(final String sessionId) throws InterruptedException {
        final String generatedUsername = usernameService.generateUsername();
        final User addedUser = new User(generatedUsername, sessionId);
        System.out.println("addedUser = " + addedUser);
        activeUsers.put(sessionId, addedUser);
        presenceController.publishLoginInfo(addedUser);
    }

    public void userLoggedOut(final String sessionId) throws InterruptedException {
        final User removedUser = activeUsers.remove(sessionId);
        presenceController.publishLogOutInfo(removedUser);
    }

    public User getUser(String sessionId){
        return activeUsers.get(sessionId);
    }

    public Collection<User> getAllActiveUsers(){
        return activeUsers.values();
    }
}
