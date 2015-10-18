package org.berlinvegan.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;

import java.io.File;
import java.io.Writer;
import java.util.*;

/**
 * User: smeier Date: 3/30/13 Time: 10:09 PM
 */
public class MapGenerator extends WebsiteGenerator {

    public MapGenerator() throws Exception {
        super();
    }

    public static void main(String[] args) throws Exception {

        if (args.length >= 2) {  // output parameter with value
            parseOptions(args);
            MapGenerator generator = new MapGenerator();
            generator.generateMap("de");
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generatemap", constructOptions());
        }

    }

    public void generateMap(String language) throws Exception {
        if (!StringUtils.isEmpty(outputDir)) {
            generateMap(language, getGastroLocationFromServer());

        }
    }

    public void generateMap(String language, List<GastroLocation> gastroLocations) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));

        final HashSet<String> districts = new HashSet<String>();
        for (GastroLocation gastroLocation : gastroLocations) {
            final String district = gastroLocation.getDistrict();
            if (!district.equalsIgnoreCase("")) {
                districts.add(district);
            }
        }
        // Configuration
        Writer fileWriter = null;
        Configuration cfg = new Configuration();

        try {
            // data-model
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("reviewbase", REVIEW_BASE_LOCATION_DE);
            input.put("i18n", bundle);
            input.put("language", language);
            input.put("restaurants", gastroLocations);
            input.put("districts", districts);

            // Set Directory for templates
            cfg.setClassForTemplateLoading(MapGenerator.class, "");
            // load template

            Template template = cfg.getTemplate("map_v2.ftl", "ISO-8859-1");
            template.setOutputEncoding("UTF-8");
            fileWriter = getUTF8Writer(outputDir + File.separator + "map.html");
            template.process(input, fileWriter);
            fileWriter.flush();


        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception ignored) {
                }
            }

        }

    }

}
