package org.berlinvegan.generators;

import com.google.gson.Gson;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;
import org.berlinvegan.generators.model.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class GastroLocationJsonGenerator extends WebsiteGenerator {
    // 30 seconds
    public static final int TIMEOUT = 30 * 1000;

    public GastroLocationJsonGenerator() throws Exception {
        super();
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 4) {  // 2 options with 1 value
            parseOptions(args);
            GastroLocationJsonGenerator generator = new GastroLocationJsonGenerator();
            generator.generate();
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generate json", constructOptions(false));
        }
    }

    private void generate() throws Exception {
        final List<GastroLocation> gastroLocations = getGastroLocationFromServer();
        augmentWithReviewsAndPictures(gastroLocations);
        String json = new Gson().toJson(gastroLocations);
        if (StringUtils.isNotEmpty(json)) {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println(json);
        }
    }
    
    private void augmentWithReviewsAndPictures(List<GastroLocation> gastroLocations) throws IOException {
        for (GastroLocation gastroLocation : gastroLocations) {
            augmentWithReviewAndPictures(gastroLocation);
        }
    }
    
    private void augmentWithReviewAndPictures(GastroLocation gastroLocation) throws IOException {
        
        String reviewURL = gastroLocation.getReviewURL();
        
        if (reviewURL != null && !reviewURL.isEmpty()) {
            Document doc = Jsoup.connect(REVIEW_DE_BASE_URL + reviewURL).timeout(TIMEOUT).get();
            String review = getLocationTextFromWebsite(doc);
            review = textEncode(review);
            review = hyphenate(review, LANG_DE);
            gastroLocation.setComment(review);

            final List<Picture> pictures = getLocationPicturesFromWebsite(doc);
            gastroLocation.setPictures(pictures);
        }
    }
}
