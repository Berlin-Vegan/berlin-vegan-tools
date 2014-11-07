package org.berlinvegan.generators;

import com.google.gson.Gson;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TestUtil {
    /** download the restaurant database, convert to json, and store in filesystem*/
    public void generateExampleRestaurantJsonFile() throws Exception {
        final MapGenerator generator = new MapGenerator();
        final ArrayList<Restaurant> restaurants = generator.getRestaurantsFromServer();
        String json = new Gson().toJson(restaurants);
        final String jsonFile = getTempDir() + File.pathSeparator + "restaurant_db.json";
        Files.write(Paths.get(jsonFile), json.getBytes());

    }
    
    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

}
