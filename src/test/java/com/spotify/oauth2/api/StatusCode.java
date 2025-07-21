package com.spotify.oauth2.api;

public enum StatusCode {

    
    STATUS_CODE_200(200, ""),
    STATUS_CODE_201(201, ""),
    STATUS_CODE_400(400, "Missing required field: name"),
    STATUS_CODE_401(401, "Invalid access token");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {

        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }


}
