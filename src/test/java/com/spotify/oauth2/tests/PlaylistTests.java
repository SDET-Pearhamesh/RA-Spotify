package com.spotify.oauth2.tests;

import com.spotify.oauth2.Utils.PlaylistAPI;
import com.spotify.oauth2.api.SpecBuilder;
import com.spotify.oauth2.pojo.ErrorClass;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {


    public Playlist playlistBuilder(String playlistName , String playlistDescription, boolean isPlaylistPublic){

        return new Playlist().
                setName(playlistName).
                setDescription(playlistDescription).
                setPublic(isPlaylistPublic);
    }

    public void assertPlaylistEqual(Playlist responsePlaylist , Playlist requestPlaylist){

        assertThat(responsePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }

    public void assertStatuscode(int actualStatuscode , int expectedStatuscode){
        assertThat(actualStatuscode , equalTo(expectedStatuscode));
    }



@Test(description = "This method is used to verify the authenticated user can create a playlist" , priority = 1)
public void createPlaylist(){

    Playlist requestPlaylist = playlistBuilder("REST ASSURED PLAYLIST" ,"This playlist is created using REST Assured" , false);

    Response response = PlaylistAPI.post(requestPlaylist);



}



    @Test(description = "This method is used to verify the authenticated user can update a playlist" , priority = 2)
    public void updatePlaylist(){

        String PlaylistID = "";
    
        Playlist requestPlaylist = new Playlist().
                setName("REST ASSURED UPDATED PLAYLIST NAME").
                setDescription("This playlist is updated using REST Assured").
                setPublic(false);

        Response response = PlaylistAPI.update(requestPlaylist , PlaylistID);
        assertThat(response.statusCode() , equalTo(200));



    }

    @Test(description = "This method is used to verify the authenticated user can get newly created playlist" , priority = 3)
    public void getPlaylist(){

        Playlist requestPlaylist = new Playlist().
                setName("REST ASSURED UPDATED PLAYLIST NAME").
                setDescription("This playlist is updated using REST Assured").
                setPublic(false);

        String PlaylistID = "";

        Response response = PlaylistAPI.get(PlaylistID);

        Playlist responseCreatePlaylist = response.as(Playlist.class);  // DESERIALIZATION

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }

    @Test(description = "This method is used to verify the authenticated user can create a playlist with empty name" , priority = 4)
    public void noNameToCreatePlaylist(){

        Playlist requestPlaylist = new Playlist().
                setName("REST ASSURED UPDATED PLAYLIST NAME").
                setDescription("This playlist is updated using REST Assured").
                setPublic(false);

        Response response = PlaylistAPI.post(requestPlaylist);
        ErrorClass error  = response.as(ErrorClass.class);


       assertThat(error.getError().getStatus() , equalTo(400));
       assertThat(error.getError().getMessage() , equalTo("Missing required field: name"));
    }

    @Test(description = "This method is used to verify with invalid token too create a playlist" , priority = 5)
    public void expiredTokenToCreatePlaylist(){

        Playlist requestPlaylist = new Playlist().
                setName("REST ASSURED UPDATED PLAYLIST NAME").
                setDescription("This playlist is updated using REST Assured").
                setPublic(false);

        String PlaylistID = "";
        Response response = PlaylistAPI.post(requestPlaylist , PlaylistID);
        ErrorClass error  = response.as(ErrorClass.class);

        assertThat(error.getError().getStatus() , equalTo(401));
        assertThat(error.getError().getMessage() , equalTo("Invalid access token"));

    }


    @Test(description = "This method is used to verify the authenticated user can delete a playlist" , priority = 6)
    public void deletePlaylist(){

        Playlist requestPlaylist = new Playlist().
                setName("REST ASSURED UPDATED PLAYLIST NAME").
                setDescription("This playlist is updated using REST Assured").
                setPublic(false);

        String PlaylistID = "";
        Response response = PlaylistAPI.get(PlaylistID);

        Playlist responseCreatePlaylist = response.as(Playlist.class);  // DESERIALIZATION

        assertThat(responseCreatePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responseCreatePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responseCreatePlaylist.getPublic() , equalTo(requestPlaylist.getPublic()));

    }



}
