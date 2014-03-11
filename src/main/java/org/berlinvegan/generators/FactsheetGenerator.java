package org.berlinvegan.generators;

import com.google.gdata.util.AuthenticationException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class FactsheetGenerator extends WebsiteGenerator {
    public FactsheetGenerator() throws AuthenticationException {
        super();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 6 || args.length == 8) {  // 3 or 4 options with 1 value
            parseOptions(args);
            FactsheetGenerator generator = new FactsheetGenerator();
            generator.generateFactSheets("de");
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generatefactsheets", constructOptions());
        }
    }

    private void generateFactSheets(String language) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));
        final ArrayList<Restaurant> restaurants = getRestaurantsFromServer();
        generateFactSheets(language, bundle, restaurants);
        if (!StringUtils.isEmpty(outputDirV2)) {
            generateFactSheetsV2(language, bundle, restaurants);
        }


    }

    private void generateFactSheetsV2(String language, ResourceBundle bundle, ArrayList<Restaurant> restaurants) {
        // Configuration
        Writer fileWriter = null;
        Configuration cfg = new Configuration();
        try {
            // Set Directory for templates
            cfg.setClassForTemplateLoading(FactsheetGenerator.class, "");
            // load template
            Template template = cfg.getTemplate("factsheet_v2.ftl", "ISO-8859-1");
            template.setOutputEncoding("UTF-8");
            // data-model
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("i18n", bundle);
            input.put("language", language);
            HashSet<String> restaurantsDone = new HashSet<String>();
            for (Restaurant restaurant : restaurants) {
                String reviewURL = restaurant.getReviewURL();
                if (!StringUtils.isEmpty(reviewURL) && !restaurantsDone.contains(reviewURL)) {
                    ArrayList<Restaurant> restaurantBranches = getBranches(reviewURL, restaurants);
                    input.put("branches", restaurantBranches);
                    // File output
                    fileWriter = getUTF8Writer(outputDirV2 + File.separator + reviewURL + ".html");
                    template.process(input, fileWriter);
                    fileWriter.flush();
                    restaurantsDone.add(reviewURL);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (Exception ignored) {
                }
            }
        }

    }

    private void generateFactSheets(String language, ResourceBundle bundle, ArrayList<Restaurant> restaurants) {
        // Configuration
        Writer file = null;
        Configuration cfg = new Configuration();
        try {
            // Set Directory for templates
            cfg.setClassForTemplateLoading(FactsheetGenerator.class, "");
            // load template
            Template template = cfg.getTemplate("factsheet.ftl", "ISO-8859-1");
            template.setOutputEncoding("ISO-8859-1");
            // data-model
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("i18n", bundle);
            input.put("language", language);
            HashSet<String> restaurantsDone = new HashSet<String>();
            for (Restaurant restaurant : restaurants) {
                String reviewURL = restaurant.getReviewURL();
                if (!StringUtils.isEmpty(reviewURL) && !restaurantsDone.contains(reviewURL)) {
                    ArrayList<Restaurant> restaurantBranches = getBranches(reviewURL, restaurants);
                    input.put("branches", restaurantBranches);
                    // File output
                    file = new FileWriter(new File(outputDir + File.separator + reviewURL + ".html"));
                    template.process(input, file);
                    file.flush();
                    restaurantsDone.add(reviewURL);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception ignored) {
                }
            }
        }
    }

    /**
     * get branches(filialen) for restaurant
     *
     * @param reviewURL
     * @param restaurants
     * @return
     */
    private ArrayList<Restaurant> getBranches(String reviewURL, ArrayList<Restaurant> restaurants) {
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();
        for (Restaurant restaurant : restaurants) {
            if (StringUtils.isNotEmpty(restaurant.getReviewURL()) && restaurant.getReviewURL().equals(reviewURL)) {
                list.add(restaurant);
            }
        }
        return list;
    }


}
