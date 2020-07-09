package com.gruzini.messenger.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private User sender;
    private User recipient;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;

    public String getCreatedAt() {
        return createdAt.format(FORMATTER);
    }
}
