package org.berlinvegan.generators;

import com.google.gson.Gson;
import org.berlinvegan.generators.model.GastroLocation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestUtil {
    /** download the restaurant database, convert to json, and store in filesystem*/
    public void generateExampleRestaurantJsonFile() throws Exception {
        final MapGenerator generator = new MapGenerator();
        final List<GastroLocation> gastroLocations = generator.getGastroLocationFromServer();
        String json = new Gson().toJson(gastroLocations);
        final String jsonFile = getTempDir() + File.pathSeparator + "restaurant_db.json";
        Files.write(Paths.get(jsonFile), json.getBytes());

    }
    
    public static String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
