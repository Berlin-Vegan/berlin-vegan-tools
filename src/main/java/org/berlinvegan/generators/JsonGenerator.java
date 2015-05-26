package org.berlinvegan.generators;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.HelpFormatter;

import com.google.gdata.util.AuthenticationException;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

public class JsonGenerator extends WebsiteGenerator {
    
    public JsonGenerator() throws AuthenticationException {
        super();
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 4) {  // 2 options with 1 value
            parseOptions(args);
            JsonGenerator generator = new JsonGenerator();
            generator.generate();
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generate json", constructOptions(false));
        }
    }

    private void generate() throws Exception {
        final List<Restaurant> restaurants = getRestaurantsFromServer();
        augmentWithReviews(restaurants);
        String json = new Gson().toJson(restaurants);
        if (StringUtils.isNotEmpty(json)) {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println(json);
        }
    }
    
    private void augmentWithReviews(List<Restaurant> restaurants) throws IOException {
        for (Restaurant restaurant : restaurants) {
            augmentWithReview(restaurant);
        }
    }
    
    private void augmentWithReview(Restaurant restaurant) throws IOException {
        
        String reviewURL = restaurant.getReviewURL();
        
        if (reviewURL != null && !reviewURL.isEmpty()) {
            String review = getLocationTextFromWebsite(REVIEW_DE_BASE_URL + reviewURL);
            review = textEncode(review);
            review = hyphenate(review, LANG_DE);
            restaurant.setComment(review);
        }
    }
}
