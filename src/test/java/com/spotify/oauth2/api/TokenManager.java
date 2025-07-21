package com.spotify.oauth2.api;

import com.spotify.oauth2.Utils.ConfigLoader;
import com.spotify.oauth2.Utils.PlaylistAPI;
import io.restassured.response.Response;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TokenManager {

    private static String access_token;
    private static Instant expiry_time;

    public synchronized static String getToken(){

        try {

            if(access_token == null || Instant.now().isAfter(expiry_time)) {

                System.out.println("Renewing the token");
                Response response = renewToken();
                access_token = response.path("access_token");

                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            }
            else {
                System.out.println("Token has enough expiry time to run all tests");
            }
        }
        catch (Exception e){
            throw new RuntimeException("Failed to get token : " + e);
        }

        return access_token;
    }


    private static Response renewToken() throws FileNotFoundException {

        HashMap<String , String> formParams = new HashMap<String , String>();
        formParams.put("client_id" , ConfigLoader.getInstance("user_id").getClientID());
        formParams.put("client_secret" , ConfigLoader.getInstance("user_id").getClientSecrete() );
        formParams.put("grant_type" , ConfigLoader.getInstance("user_id").getGrantType());
        formParams.put("refresh_token" , ConfigLoader.getInstance("user_id").getRefreshToken());

       Response response =  PlaylistAPI.postAccount(formParams);

       if(response.statusCode() != 200){
           throw new RuntimeException("Token renewal failed");
       }
       return response;
    }
}
