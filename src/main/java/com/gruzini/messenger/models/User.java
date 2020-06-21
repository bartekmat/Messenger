package com.gruzini.messenger.models;

import lombok.Data;

import java.util.Random;

@Data
public class User {
    private final String username;
    private final String colorCode;

    public User(String username) {
        this.username = username;
        final Random r = new Random(this.username.hashCode());
        final int randomNum = r.nextInt(0xffffff + 1);
        this.colorCode = String.format("#%06x", randomNum);
    }
}
