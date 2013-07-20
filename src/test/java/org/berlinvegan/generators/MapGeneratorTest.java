package org.berlinvegan.generators;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;

/**
 * Date: 20.07.13
 * Time: 11:47
 */
public class MapGeneratorTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    @Test
    public void testGenerateMap() throws Exception {
        final MapGenerator generator = new MapGenerator();
        WebsiteGenerator.setOutputDir(tempFolder.getRoot().getAbsolutePath());
        final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        Restaurant restaurant = new Restaurant("name1", "Kreuzberg", "52.49905", "13.42999", 2);
        restaurants.add(restaurant);
        restaurant = new Restaurant("name2", "Mitte/Wedding", "52.48905", "13.43999", 2);
        restaurants.add(restaurant);
        generator.generateMap("de", restaurants);
    }
}
