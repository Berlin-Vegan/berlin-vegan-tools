package org.berlinvegan.generators;

import com.google.gson.Gson;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.ShoppingLocation;

import java.io.PrintStream;
import java.util.List;

public class ShoppingLocationJsonGenerator extends WebsiteGenerator {

    public ShoppingLocationJsonGenerator() throws Exception {
        super();
    }
    
    public static void main(String[] args) throws Exception {
        if (args.length == 4) {  // 2 options with 1 value
            parseOptions(args);
            ShoppingLocationJsonGenerator generator = new ShoppingLocationJsonGenerator();
            generator.generate();
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generate json", constructOptions(false));
        }
    }

    private void generate() throws Exception {
        final List<ShoppingLocation> locations = getShoppingLocationFromServer();
        String json = new Gson().toJson(locations);
        if (StringUtils.isNotEmpty(json)) {
            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println(json);
        }
    }
}
