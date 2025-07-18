package com.spotify.oauth2.tests;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.given;

public class PlaylistTests {


    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;


@BeforeTest
    public void setup(){


}

@Test
    public void createPlaylist(){

    Playlist requestPlaylist = new Playlist();
    requestPlaylist.setName("REST ASSURED PLAYLIST");
    requestPlaylist.setDescription("This playlist is created using REST Assured");
    requestPlaylist.setPublic(false);

   Playlist responseCreatePlaylist = given(requestSpecification).
            body(requestPlaylist).
        when().
            post("/sdd").
        then().spec(responseSpecification).
            assertThat().statusCode(201).
            extract().response().as(Playlist.class);

   

}

}
