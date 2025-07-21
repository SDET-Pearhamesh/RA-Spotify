package com.spotify.oauth2.tests;

import com.spotify.oauth2.Utils.JavaFakerUtils;
import com.spotify.oauth2.Utils.PlaylistAPI;
import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.pojo.ErrorClass;
import com.spotify.oauth2.pojo.Playlist;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist API - To create, get, update playlists")
public class PlaylistTests extends BaseTest {

public String playlistID;
public String playlistName;
public String getPlaylistDescription;
public boolean isPublic;


@Story("Create a playlist")
@Test(description = "This method is used to verify the authenticated user can create a playlist" , priority = 1 )
public void createPlaylist() throws FileNotFoundException {

    Playlist requestPlaylist = playlistBuilder(JavaFakerUtils.generateName(), JavaFakerUtils.generateDescription() , true);

    Response response = PlaylistAPI.post(requestPlaylist);

    assertStatuscode(response.statusCode(), StatusCode.STATUS_CODE_201.getCode());
    assertPlaylistEqual(response.as(Playlist.class) , requestPlaylist);

    playlistID = response.path("id");
    playlistName = response.path("name");
    getPlaylistDescription = response.path("description");
    isPublic = response.path("public");

}


@Story("Update a playlist")
@Test(description = "This method is used to verify the authenticated user can update a playlist" , priority = 2 )
public void updatePlaylist(){

    Playlist requestPlaylist = playlistBuilder(JavaFakerUtils.generateName(), JavaFakerUtils.generateDescription() , false);

    Response response = PlaylistAPI.update(requestPlaylist , playlistID);
    assertStatuscode(response.statusCode(), StatusCode.STATUS_CODE_200.getCode());

}


@Story("Get a playlist")
@Test(description = "This method is used to verify the authenticated user can get newly created playlist" , priority = 3 )
public void getPlaylist(){

    Playlist requestPlaylist = playlistBuilder(playlistName , getPlaylistDescription , isPublic);

    Response response = PlaylistAPI.get(playlistID);
    assertStatuscode(response.statusCode(), StatusCode.STATUS_CODE_200.getCode());
    assertPlaylistEqual(response.as(Playlist.class) , requestPlaylist);

}


@Story("Create a playlist")
@Test(description = "This method is used to verify the authenticated user cant create a playlist with empty name" , priority = 4 )
public void noNameToCreatePlaylist() throws FileNotFoundException {

     Playlist requestPlaylist = playlistBuilder("" , JavaFakerUtils.generateDescription() , false);

     Response response = PlaylistAPI.post(requestPlaylist);
     ErrorClass error  = response.as(ErrorClass.class);

     assertThat(error.getError().getStatus() , equalTo(StatusCode.STATUS_CODE_400.getCode()));
     assertThat(error.getError().getMessage() , equalTo(StatusCode.STATUS_CODE_400.getMessage()));

}


@Story("Create a playlist")
@Test(description = "This method is used to verify with expired token to create a playlist" , priority = 5 )
public void expiredTokenToCreatePlaylist() throws FileNotFoundException {

    Playlist requestPlaylist = playlistBuilder(JavaFakerUtils.generateName(), JavaFakerUtils.generateDescription() , false);

    Response response = PlaylistAPI.post(requestPlaylist , playlistID);
    ErrorClass error  = response.as(ErrorClass.class);

    assertThat(error.getError().getStatus() , equalTo(StatusCode.STATUS_CODE_401.getCode()));
    assertThat(error.getError().getMessage() , equalTo(StatusCode.STATUS_CODE_401.getMessage()));

}

    public Playlist playlistBuilder(String playlistName , String playlistDescription, boolean isPlaylistPublic){

        return Playlist.builder().
                name(playlistName).
                description(playlistDescription).
                _public(isPlaylistPublic).
                build();
    }

    public void assertPlaylistEqual(Playlist responsePlaylist , Playlist requestPlaylist){

        assertThat(responsePlaylist.getName() , equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription() , equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public() , equalTo(requestPlaylist.get_public()));

    }

    public void assertStatuscode(int actualStatuscode , int expectedStatuscode){
        assertThat(actualStatuscode , equalTo(expectedStatuscode));
    }

}
