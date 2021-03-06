package com.gruzini.messenger.services;

import com.gruzini.messenger.controllers.PresenceController;
import com.gruzini.messenger.models.LogoService;
import com.gruzini.messenger.models.MyPrincipal;
import com.gruzini.messenger.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PresenceService {

    private final UsernameService usernameService;
    private final PresenceController presenceController;
    private final LogoService logoService;

    public PresenceService(UsernameService usernameService, PresenceController presenceController, LogoService logoService) {
        this.usernameService = usernameService;
        this.presenceController = presenceController;
        this.logoService = logoService;
    }

    private Map<String, User> activeUsers = new HashMap<>();

    public void userLoggedIn(final Principal principal) throws InterruptedException {
        String generatedUsername = null;
        String avatarUrl = null;
        if (principal instanceof MyPrincipal) {
            generatedUsername = usernameService.generateUsername();
            avatarUrl = logoService.getLogoLink(generatedUsername);
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2User user = ((OAuth2AuthenticationToken) principal).getPrincipal();
            generatedUsername = user.getAttribute("name");
            avatarUrl = user.getAttribute("avatar_url");
        }

        final User addedUser = new User(generatedUsername, principal.getName(), avatarUrl);
        activeUsers.put(principal.getName(), addedUser);
        presenceController.publishLoginInfo(addedUser);
    }

    public void userLoggedOut(final String principalName) throws InterruptedException {
        final User removedUser = activeUsers.remove(principalName);
        presenceController.publishLogOutInfo(removedUser);
    }

    public User getUser(final String principalName) {
        return activeUsers.get(principalName);
    }

    public User getUserByName(final String name) {
        return getAllActiveUsers().stream().filter(user -> user.getUsername().equals(name)).findFirst().orElseThrow();
    }

    public Collection<User> getAllActiveUsers() {
        return activeUsers.values();
    }
}
