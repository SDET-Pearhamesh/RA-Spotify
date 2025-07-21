package com.spotify.oauth2.Utils;

import com.spotify.oauth2.api.Routes;
import com.spotify.oauth2.api.SpecBuilder;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class PlaylistAPI {


    public static Response post(Playlist requestPlaylist) throws FileNotFoundException {

        return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                auth().oauth2(TokenManager.getToken()).
                when().
                post( Routes.USERS + "/31uybx2xzlsvo6kskyxhgezb4aqe" + Routes.PLAYLISTS).   // value is hard coded, need to change this
                        then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response post(Playlist requestPlaylist, String invalidToken) {

        return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                auth().oauth2(TokenManager.getToken()).
                when().
                post(Routes.USERS + "/sdd" + Routes.PLAYLISTS).
                        then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response get(String PlaylistID) {

        return given(SpecBuilder.getRequestSpec()).
                auth().oauth2(TokenManager.getToken()).
                when().
                get(Routes.PLAYLISTS + "/sendid").
                then().spec(SpecBuilder.getResponseSpec()).
                assertThat().statusCode(200).
                extract().response();

    }


    public static Response update(Playlist requestPlaylist, String PlaylistID) {

        return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                auth().oauth2(TokenManager.getToken()).
                when().
                put(Routes.PLAYLISTS + "/sendid").
                then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response postAccount(HashMap<String, String> formParams) {

        return given(SpecBuilder.getAccountRequestSpec()).
                baseUri("https://accounts.spotify.com").
                formParams(formParams).
        when().
                post(Routes.API + Routes.TOKEN).
        then().
                spec(SpecBuilder.getResponseSpec()).
                extract().
                response();
    }

}
