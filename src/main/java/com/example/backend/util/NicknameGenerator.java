package com.example.backend.util;

import java.util.Random;

public class NicknameGenerator {
    private static final String[] adjectives = {"Clever", "Sneaky", "Gentle", "Brave", "Witty", "Cunning", "Swift", "Happy", "Silly", "Fierce"};
    private static final String[] nouns = {"Fox", "Bear", "Tiger", "Lion", "Wolf", "Penguin", "Eagle", "Dolphin", "Dragon", "Phoenix"};

    private NicknameGenerator() {
    }
    public static String generateNickname() {
        Random rand = new Random();
        String adjective = adjectives[rand.nextInt(adjectives.length)];
        String noun = nouns[rand.nextInt(nouns.length)];
        return adjective + " " +  noun;
    }
    public static String[] generateNicknames(int numNicknames) {
        String[] nicknames = new String[numNicknames];
        Random rand = new Random();

        for (int i = 0; i < numNicknames; i++) {
            String adjective = adjectives[rand.nextInt(adjectives.length)];
            String noun = nouns[rand.nextInt(nouns.length)];
            nicknames[i] = adjective + noun;
        }

        return nicknames;
    }
}
