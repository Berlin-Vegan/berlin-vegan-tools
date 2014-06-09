package org.berlinvegan.generators;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class TestUtil {
    public void generateExampleRestaurantJsonFile() throws Exception {
        //final ArrayList<String> parameters = constructParameters();
        //WebsiteGenerator.parseOptions(parameters.toArray(new String[parameters.size()]));// set static vars, todo refactor
        final MapGenerator generator = new MapGenerator();
        final ArrayList<Restaurant> restaurants = generator.getRestaurantsFromServer();
        String json = new Gson().toJson(restaurants);
        Files.write(Paths.get("/tmp/restaurant_db.json"), json.getBytes());

    }

}
