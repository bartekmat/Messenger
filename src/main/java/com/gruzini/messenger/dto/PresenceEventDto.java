package com.gruzini.messenger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresenceEventDto {

    public static enum PresenceEventType{
        LOGGED_IN, LOGGED_OUT
    }

    private String username;
    private String colorCode;
    private String logoLink;
    private PresenceEventType type;
}
