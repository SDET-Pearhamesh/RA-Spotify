package com.spotify.oauth2.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter // Just add these two annotations to add setters and getters
@Builder // Creates builder pattern static method that return the Build class
@Jacksonized // To let lombok understand the annotations used Jackson dependency like below one

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorClass {


        @JsonProperty("error")
        private InnerError error;


}

