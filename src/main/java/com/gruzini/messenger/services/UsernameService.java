package com.gruzini.messenger.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@SessionScope
@Data
public class UsernameService {
    private static Set<String> generatedNames = new HashSet<>();
    private static List<String> availableNames = new LinkedList<>();
    private static int lastGeneratedIndex = 0;

    private static String[] names = {"Dog", "Cat"};
    private String username = null;

    public String getUsername() {
        if (isNull(username)){
            username = generateUsername();
        }
        return username;
    }

    private String generateUsername() {
        if (availableNames.size() == 0){
            lastGeneratedIndex++;
            availableNames.addAll(
                    Arrays.stream(names)
                            .map(name -> name + (lastGeneratedIndex == 1 ? "" : lastGeneratedIndex))
                            .collect(Collectors.toList()));
        }
        final int randomIndex = new Random().nextInt(names.length-1);
        final String name = availableNames.remove(randomIndex);
        generatedNames.add(name);
        return name;
    }
}
