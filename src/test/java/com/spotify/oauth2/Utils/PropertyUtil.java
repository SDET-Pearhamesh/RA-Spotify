package com.spotify.oauth2.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {

    public static Properties propertyLoader(String filePath) throws FileNotFoundException {


        Properties properties = new Properties();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed to load properties file " + filePath, e);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("File not found at  " + filePath, e);

        }
        return properties;
    }
}
