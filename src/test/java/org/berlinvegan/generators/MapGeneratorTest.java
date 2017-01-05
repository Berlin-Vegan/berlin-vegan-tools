package org.berlinvegan.generators;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.berlinvegan.generators.model.GastroLocation;
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

public class MapGeneratorTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testGenerateCompleteMap() throws Exception {
        List<GastroLocation> gastroLocations = getLocalGastroLocationDB();
        final MapGenerator generator = new MapGenerator();
        WebsiteGenerator.setOutputDir(TestUtil.getTempDir());
        generator.generateMap("de", gastroLocations);
        System.out.println("map generated: " + TestUtil.getTempDir() + "/map.html");
    }

    @Test
    public void testGenerateMap() throws Exception {
        final MapGenerator generator = new MapGenerator();
        final String outputDir = tempFolder.getRoot().getAbsolutePath();
        WebsiteGenerator.setOutputDir(outputDir);
        final ArrayList<GastroLocation> gastroLocations = new ArrayList<GastroLocation>();
        GastroLocation gastroLocation = new GastroLocation("name1", "Kreuzberg", 52.49905, 13.42999, 2);
        gastroLocations.add(gastroLocation);
        gastroLocation = new GastroLocation("name2", "Mitte/Wedding", 52.48905, 13.43999, 2);
        gastroLocations.add(gastroLocation);
        generator.generateMap("de", gastroLocations);
    }



    private List<GastroLocation> getLocalGastroLocationDB() throws URISyntaxException, IOException {
        final URL url = getClass().getResource("gastrolocation_db.json");
        final Path path = Paths.get(url.toURI());
        final String json = new String(Files.readAllBytes(path));
        return new Gson().fromJson(json, new TypeToken<ArrayList<GastroLocation>>() {}.getType());
    }

}
