package com.spotify.oauth2.tests;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {


    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;


@BeforeTest
    public void setup(){


}

@Test(description = "This method is used to verify the authenticated user can create a playlist")
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

   assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
   assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
   assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

}

    @Test(description = "This method is used to verify the authenticated user can get newly created playlist")
    public void getPlaylist(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("REST ASSURED PLAYLIST");
        requestPlaylist.setDescription("This playlist is created using REST Assured");
        requestPlaylist.setPublic(false);

        Playlist responseCreatePlaylist =

        given(requestSpecification).
        when().
                get("/sdd").
        then().spec(responseSpecification).
                assertThat().statusCode(200).
                extract().response().as(Playlist.class);

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }

    @Test(description = "This method is used to verify the authenticated user can update a playlist")
    public void updatePlaylist(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("REST ASSURED UPDATED PLAYLIST NAME");
        requestPlaylist.setDescription("This playlist is updated using REST Assured");
        requestPlaylist.setPublic(false);

        Playlist responseCreatePlaylist = given(requestSpecification).
                body(requestPlaylist).
                when().
                post("/sdd").
                then().spec(responseSpecification).
                assertThat().statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }

    @Test(description = "This method is used to verify the authenicated user can update a playlist")
    public void invalidToCreatePlaylist(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("REST ASSURED UPDATED PLAYLIST NAME");
        requestPlaylist.setDescription("This playlist is updated using REST Assured");
        requestPlaylist.setPublic(false);

        Playlist responseCreatePlaylist = given(requestSpecification).
                body(requestPlaylist).
                when().
                post("/sdd").
                then().spec(responseSpecification).
                assertThat().statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }

    @Test(description = "This method is used to verify the authenicated user can update a playlist")
    public void createPlaylistUsingExpiredToken(){

        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("REST ASSURED UPDATED PLAYLIST NAME");
        requestPlaylist.setDescription("This playlist is updated using REST Assured");
        requestPlaylist.setPublic(false);

        Playlist responseCreatePlaylist = given(requestSpecification).
                body(requestPlaylist).
                when().
                post("/sdd").
                then().spec(responseSpecification).
                assertThat().statusCode(401).
                extract().response().as(Playlist.class);

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }




    @Test(description = "This method is used to verify the authenticated user can delete a playlist")
    public void deletePlaylist(){

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

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }



}
