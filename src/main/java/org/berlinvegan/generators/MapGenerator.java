package org.berlinvegan.generators;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import org.apache.commons.cli.HelpFormatter;

import com.google.gdata.util.AuthenticationException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;

/**
 * User: smeier Date: 3/30/13 Time: 10:09 PM
 */
public class MapGenerator extends WebsiteGenerator {

    public MapGenerator() throws AuthenticationException {
    }


    public static void main(String[] args) throws Exception {

        if (args.length == 6 || args.length == 8) {  // 3 or 4 options with 1 value
            parseOptions(args);
            MapGenerator generator = new MapGenerator();
            generator.generateMap("de");
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generatemap", constructOptions());
        }

    }

    public void generateMap(String language) throws Exception {
        generateMap(language, getRestaurantsFromServer());
        if (!StringUtils.isEmpty(outputDirV2)) {
            generateMapV2(language, getRestaurantsFromServer());

        }
    }

    private void generateMapV2(String language, ArrayList<Restaurant> restaurants) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));

        final HashSet<String> districts = new HashSet<String>();
        for (Restaurant restaurant : restaurants) {
            final String district = restaurant.getDistrict();
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
            input.put("reviewbase", REVIEW_BASE_LOCATION_DE_V2);
            input.put("i18n", bundle);
            input.put("language", language);
            input.put("restaurants", restaurants);
            input.put("districts", districts);

            // Set Directory for templates
            cfg.setClassForTemplateLoading(MapGenerator.class, "");
            // load template

            Template template = cfg.getTemplate("map_v2.ftl", "ISO-8859-1");
            template.setOutputEncoding("UTF-8");
            fileWriter = getUTF8Writer(outputDirV2 + File.separator + "map.html");
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

    public void generateMap(String language, ArrayList<Restaurant> restaurants) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));

        final HashSet<String> districts = new HashSet<String>();
        for (Restaurant restaurant : restaurants) {
            final String district = restaurant.getDistrict();
            if (district != null && !district.equalsIgnoreCase("")) {
                districts.add(district);
            }
        }
        // Configuration
        Writer file = null;
        Configuration cfg = new Configuration();

        try {
            // data-model
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("reviewbase", REVIEW_BASE_LOCATION_DE);
            input.put("i18n", bundle);
            input.put("language", language);
            input.put("restaurants", restaurants);
            input.put("districts", districts);

            // Set Directory for templates
            cfg.setClassForTemplateLoading(MapGenerator.class, "");
            // load template
            Template template = cfg.getTemplate("map.ftl", "ISO-8859-1");
            template.setOutputEncoding("ISO-8859-1");
            // File output
            file = new FileWriter(new File(outputDir + File.separator + "map.html"));
            template.process(input, file);
            file.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception ignored) {
                }
            }
        }

    }
}
