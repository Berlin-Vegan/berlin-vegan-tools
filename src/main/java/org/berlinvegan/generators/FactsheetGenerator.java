package org.berlinvegan.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;

import java.io.File;
import java.io.Writer;
import java.util.*;

public class FactsheetGenerator extends WebsiteGenerator {
    public FactsheetGenerator() throws Exception {
        super();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 6) {  // 3 options with 1 value
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
        final List<GastroLocation> gastroLocations = getGastroLocationFromServer();
        if (!StringUtils.isEmpty(outputDir)) {
            generateFactSheets(language, bundle, gastroLocations);
        }


    }

    private void generateFactSheets(String language, ResourceBundle bundle, List<GastroLocation> gastroLocations) {
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
            Set<String> restaurantsDone = new HashSet<String>();
            for (GastroLocation gastroLocation : gastroLocations) {
                String reviewURL = gastroLocation.getReviewURL();
                if (!StringUtils.isEmpty(reviewURL) && !restaurantsDone.contains(reviewURL)) {
                    List<GastroLocation> gastroLocationBranches = getBranches(reviewURL, gastroLocations);
                    input.put("branches", gastroLocationBranches);
                    // File output
                    fileWriter = getUTF8Writer(outputDir + File.separator + reviewURL + ".html");
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


    /**
     * get branches(filialen) for restaurant
     *
     * @param reviewURL
     * @param gastroLocations
     * @return
     */
    private List<GastroLocation> getBranches(String reviewURL, List<GastroLocation> gastroLocations) {
        ArrayList<GastroLocation> list = new ArrayList<GastroLocation>();
        for (GastroLocation gastroLocation : gastroLocations) {
            String url = gastroLocation.getReviewURL();
            if (StringUtils.isNotEmpty(url) && url.equals(reviewURL)) {
                list.add(gastroLocation);
            }
        }
        return list;
    }


}
