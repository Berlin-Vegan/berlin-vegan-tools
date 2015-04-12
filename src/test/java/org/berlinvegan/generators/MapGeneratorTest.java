package org.berlinvegan.generators;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 20.07.13
 * Time: 11:47
 */
public class MapGeneratorTest {
    @Rule
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testGenerateCompleteMap() throws Exception {
        List<Restaurant> restaurants = getLocalRestaurantsDB();
        final MapGenerator generator = new MapGenerator();
        WebsiteGenerator.setOutputDir(TestUtil.getTempDir());
        generator.generateMap("de", restaurants);
        System.out.println("map generated: " + TestUtil.getTempDir() + "/map.html");
    }

    @Test
    public void testGenerateMap() throws Exception {
        final MapGenerator generator = new MapGenerator();
        final String outputDir = tempFolder.getRoot().getAbsolutePath();
        WebsiteGenerator.setOutputDir(outputDir);
        final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        Restaurant restaurant = new Restaurant("name1", "Kreuzberg", 52.49905, 13.42999, 2);
        restaurants.add(restaurant);
        restaurant = new Restaurant("name2", "Mitte/Wedding", 52.48905, 13.43999, 2);
        restaurants.add(restaurant);
        generator.generateMap("de", restaurants);
    }



    private List<Restaurant> getLocalRestaurantsDB() throws URISyntaxException, IOException {
        final URL url = getClass().getResource("restaurant_db.json");
        final Path path = Paths.get(url.toURI());
        final String json = new String(Files.readAllBytes(path));
        return new Gson().fromJson(json, new TypeToken<ArrayList<Restaurant>>() {}.getType());
    }

}
