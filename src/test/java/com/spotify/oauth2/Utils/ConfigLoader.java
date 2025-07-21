package com.spotify.oauth2.Utils;

import java.io.FileNotFoundException;
import java.util.Properties;

public class ConfigLoader {

    private  final Properties properties;
    private static ConfigLoader configLoader;

    private ConfigLoader() throws FileNotFoundException {
        properties = PropertyUtil.propertyLoader("src/test/resources/config.properties");
    }

    public static ConfigLoader getInstance(String userId) throws FileNotFoundException {

        if(configLoader == null){
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getClientID(){
        String prop = properties.getProperty("client_id");
        if(prop != null) return prop;
        else throw new RuntimeException("Specify the Client id in the config.property file");
    }

    public String getClientSecrete(){
        String prop = properties.getProperty("client_secret");
        if(prop != null) return prop;
        else throw new RuntimeException("Specify the client_secret id in the config.property file");
    }

    public String getGrantType(){
        String prop = properties.getProperty("grant_type");
        if(prop != null) return prop;
        else throw new RuntimeException("Specify the grant_type in the config.property file");
    }

    public String getRefreshToken(){
        String prop = properties.getProperty("refresh_token");
        if(prop != null) return prop;
        else throw new RuntimeException("Specify the refresh_token in the config.property file");
    }

    public String getExpiredToken(){
        String prop = properties.getProperty("expired_token");
        if(prop != null) return prop;
        else throw new RuntimeException("Specify the expired_token in the config.property file");
    }



}
