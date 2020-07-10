package com.gruzini.messenger.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Random;

@Data
@ToString
@EqualsAndHashCode
public class User {
    private final String username;
    private final String colorCode;
    private final String principalName;
    private final String logoLink;

    public User(String username, String principalName, String logoLink) {
        this.username = username;
        final Random r = new Random(this.username.hashCode());
        final int randomNum = r.nextInt(0xffffff + 1);
        this.colorCode = String.format("#%06x", randomNum);
        this.principalName = principalName;
        this.logoLink = logoLink;
    }
}
