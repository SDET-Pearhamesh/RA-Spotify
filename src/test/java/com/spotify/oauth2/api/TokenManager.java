package com.spotify.oauth2.api;

import com.spotify.oauth2.Utils.PlaylistAPI;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class TokenManager {

    private static String access_token;
    private static Instant expiry_time;

    public static String getToken(){

        try {

            if(access_token == null || Instant.now().isAfter(expiry_time)) {

                System.out.println("Renewing the token");
                Response response = renewToken();
                access_token = response.path("access_token");

                long expiryDurationInSeconds = response.path("expires_in");
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

    private static Response renewToken(){

        HashMap<String , String> formParams = new HashMap<String , String>();
        formParams.put("client_id" , "c3a11e1cccfa4096ad7488d399c2502f");
        formParams.put("client_secret" , "722c14c1e25f41cda7673eaf5f9a4bfd");
        formParams.put("grant_type" , "refresh_token");
        formParams.put("refresh_token" , "AQCEBNw0kWeR-v32Xa9NrrKGB_RLFtyDEPwO107m7NRRF6-JaXawtwSTae3iv5rw6lmbCiQFARfb7iMpM3-YjToH77yxNhc4Yg2EYxpaVJ9R4kvoCgfXUjmtI-ugzGFoE8s");

       Response response =  PlaylistAPI.postAccount(formParams);

       if(response.statusCode() != 200){
           throw new RuntimeException("Token renewal failed");
       }
       return response;
    }
}
