package com.spotify.oauth2.Utils;

import com.github.javafaker.Faker;

public class JavaFakerUtils {

    public static String generateName(){

        Faker faker = new Faker();
        return "Playlist " + faker.regexify("A-Za-z0-9 ,_-]{12}"); // accepted characters and length
    }

    public static String generateDescription(){

        Faker faker = new Faker();
        return "Description " + faker.regexify("A-Za-z0-9 ,_-]{25}"); // accepted characters and length
    }
}
