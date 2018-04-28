package org.berlinvegan.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;

import java.io.File;
import java.io.Writer;
import java.util.*;

public class ListGenerator extends WebsiteGenerator {
    public ListGenerator() throws Exception {
        super();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 6) {  // only output dir option
            parseOptions(args);
            ListGenerator generator = new ListGenerator();
            generator.generateList("de");
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generatelist", constructOptions());
        }
    }

    private void generateList(String language) throws Exception {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));
        final List<GastroLocation> gastroLocations = getGastroLocationDataFromServer(false);
        if (!StringUtils.isEmpty(outputDir)) {
            generateListV2(language, bundle, gastroLocations);
        }
    }

    private void generateListV2(String language, ResourceBundle bundle, List<GastroLocation> gastroLocations) {
        // Configuration
        Writer fileWriter = null;
        try {
            // Set Directory for templates
            Configuration cfg = new Configuration();
            cfg.setClassForTemplateLoading(ListGenerator.class, "");
            // load template
            Template template = cfg.getTemplate("list_v2.ftl", "ISO-8859-1");
            template.setOutputEncoding("ISO-8859-1");
            // data-model
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("reviewbase", REVIEW_BASE_LOCATION_DE);
            input.put("i18n", bundle);
            input.put("language", language);
            List<GastroLocation> uniqueGastroLocations = getUniqueRestaurants(gastroLocations);
            input.put("restaurants", uniqueGastroLocations);

            // File output
            fileWriter = getUTF8Writer(outputDir + File.separator + "list.html");
            template.process(input, fileWriter);
            fileWriter.flush();
        } catch (Exception e) {
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
     * only 1 restaurant instance per name, without branches and only gastroLocations with review
     */
    private static List<GastroLocation> getUniqueRestaurants(List<GastroLocation> gastroLocations) {
        ArrayList<GastroLocation> result = new ArrayList<GastroLocation>();
        Set<String> restaurantsDone = new HashSet<String>();
        for (GastroLocation gastroLocation : gastroLocations) {
            String reviewURL = gastroLocation.getReviewURL();
            if (!StringUtils.isEmpty(reviewURL) && !restaurantsDone.contains(reviewURL)) {
                result.add(gastroLocation);
                restaurantsDone.add(reviewURL);
            }
        }
        return result;
    }
}
