package com.spotify.oauth2.Utils;

import com.spotify.oauth2.api.SpecBuilder;
import com.spotify.oauth2.api.TokenManager;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class PlaylistAPI {


    public static Response post(Playlist requestPlaylist){

       return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                header("Authorization" , "Bearer " + TokenManager.getToken()).
        when().
                post("/sdd").   // Need to add endpoint
        then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response post(Playlist requestPlaylist , String invalidToken){

        return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                header("Authorization" , "Bearer " + TokenManager.getToken()).
        when().
                post("/sdd").   // Need to add endpoint
        then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response get(String PlaylistID){

       return given(SpecBuilder.getRequestSpec()).
               header("Authorization" , "Bearer " + TokenManager.getToken()).
       when().
                get("/playlists/sendid").
       then().spec(SpecBuilder.getResponseSpec()).
                assertThat().statusCode(200).
                extract().response();

    }


    public static Response update(Playlist requestPlaylist , String PlaylistID){

        return given(SpecBuilder.getRequestSpec()).
                body(requestPlaylist).
                header("Authorization" , "Bearer " + TokenManager.getToken()).
        when().
                put("/sdd").
        then().spec(SpecBuilder.getResponseSpec()).
                extract().response();

    }

    public static Response postAccount(HashMap<String , String> formParams){

        return  given(SpecBuilder.getAccountRequestSpec()).
                       baseUri("https://accounts.spotify.com").
                       formParams(formParams).
                when().
                       post("/api/token").
                then().
                       spec(SpecBuilder.getResponseSpec()).
                       extract().
                       response();
    }


}
