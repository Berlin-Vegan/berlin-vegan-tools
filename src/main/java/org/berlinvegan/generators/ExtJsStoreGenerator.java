package org.berlinvegan.generators;

import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import net.davidashen.text.Hyphenator;
import org.apache.commons.cli.HelpFormatter;

import java.io.*;
import java.util.*;

public class ExtJsStoreGenerator extends WebsiteGenerator {

    public static final String REVIEW_DE_BASE_URL = "http://www.berlin-vegan.de/essen-und-trinken/kritiken/";
    public static final String EXT_NAMESPACE_BVAPP = "Ext.namespace('BVApp','BVApp.data','BVApp.models');";

    public ExtJsStoreGenerator() throws AuthenticationException {
    }


    public static void main(String[] args) throws Exception {

        if (args.length == 6) {  // 3 options with 1 value -> 6 cli args
            parseOptions(args);
            ExtJsStoreGenerator generator = new ExtJsStoreGenerator();
            generator.generateLocationDataStores();
            generator.generateTextfilesJS();
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generateextjsstore", constructOptions());
        }

    }

    private String hyphenate(String text, String language) throws IOException {
        Hyphenator h = new Hyphenator();
//        String hyphenBasePath = "generators" + File.separator + "lib" + File.separator + "hyphen" + File.separator;
        InputStream fileInputStream;
        if (language.equals(LANG_DE)) {
            fileInputStream = this.getClass().getClassLoader().getResourceAsStream("org/berlinvegan/generators/dehyphx.tex");
        } else {
            fileInputStream = this.getClass().getClassLoader().getResourceAsStream("org/berlinvegan/generators/hyphen.tex");
        }
        h.loadTable(fileInputStream);

        text = h.hyphenate(text, 4, 3);
        text = text.replaceAll("\u00ad", "&shy;");
        return text;
    }

    private void generateTextfilesJS() throws Exception {
        // first read the files from disk
        // <path,content>
        HashMap<String, String> filesMap = new HashMap<String, String>();
        String textFilesBase = "data" + File.separator;
        List<File> files = getFileListing(new File(textFilesBase));
        for (File file : files) {
            String text = readFileAsString(file.getPath());
            text = textEncode(text);
            String path = file.getPath();
            path = path.replaceAll("\\\\", "/"); // normalize path seperator
            path = path.replaceAll("data/", "");
            //hyphenate
            if (path.contains("/de/")) {
                text = hyphenate(text, LANG_DE);
            } else if (path.contains("/en/")) {
                text = hyphenate(text, LANG_EN);
            }
            filesMap.put(path, text);
        }
        // scrape data from website for all locations

        final List<Restaurant> restaurants = getRestaurantsFromServer();
        if (restaurants != null) {
            for (Restaurant restaurant : restaurants) {
                String reviewURL = restaurant.getReviewURL();
                if (reviewURL != null && !reviewURL.isEmpty()) {
                    String locationText = getLocationTextFromWebsite(REVIEW_DE_BASE_URL + reviewURL);
                    locationText = textEncode(locationText);
                    locationText = hyphenate(locationText, LANG_DE);
                    filesMap.put("reviews/de/" + reviewURL + ".html", locationText);
                } else { // no review available, take the short comment, just set the reviewURL to restaurant name
                    String comment = restaurant.getComment();
                    if (comment != null) {
                        comment = textEncode(comment);
                        filesMap.put("reviews/de/" + restaurant.getName() + ".html", comment);
                    }

                }
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(EXT_NAMESPACE_BVAPP);
        builder.append("BVApp.models.Data= [];");
        for (String fileName : filesMap.keySet()) {
            builder.append("\nBVApp.models.Data[\"").append(fileName).append("\"] =\"").append(filesMap.get(fileName)).append("\";");
        }
        writeTextToFile(builder.toString(), outputDir + File.separator + "Textfiles.js");
    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();    
    }

    public void generateLocationDataStores() throws Exception {
        List<ListEntry> restaurantEntries = null;
        List<ListEntry> shoppingEntries = null;
        List<ListEntry> cafeEntries = null;
        for (SpreadsheetEntry spreadsheet : getSpreadsheetEntries()) {
            String title = spreadsheet.getTitle().getPlainText();
            if (title.equalsIgnoreCase(TABLE_RESTAURANTS)) {
                restaurantEntries = addEntries(restaurantEntries, spreadsheet);
            } else if (title.equalsIgnoreCase(TABLE_SHOPPING) || title.equalsIgnoreCase(TABLE_BACKWAREN)
                    || title.equalsIgnoreCase(TABLE_BIO_REFORM)) {
                shoppingEntries = addEntries(shoppingEntries, spreadsheet);
            } else if (title.equalsIgnoreCase(TABLE_CAFES)) {
                cafeEntries = addEntries(cafeEntries, spreadsheet);
            }
        }

        // if no review available,just set restaurant name, app will then use short comment, see generateTextfilesJS()
        if (restaurantEntries != null) {
            for (ListEntry entry : restaurantEntries) {
                final Restaurant restaurant = new Restaurant(entry);
                if (restaurant.getReviewURL() == null || restaurant.getReviewURL().isEmpty()) {
                    entry.getCustomElements().setValueLocal("reviewurl", restaurant.getName());
                }
            }
        }
        generateStore(restaurantEntries, "RestaurantStoreData", outputDir);
        generateStore(shoppingEntries, "ShopStoreData", outputDir);
        generateStore(cafeEntries, "CafeStoreData", outputDir);
    }

    private void generateStore(List<ListEntry> entries, String storeName, String path) throws IOException, ServiceException {
        StringBuilder outStr = new StringBuilder();
        outStr.append(EXT_NAMESPACE_BVAPP);
        outStr.append("BVApp.data.").append(storeName).append("=[\n");
        Iterator iter = entries.iterator();
        while (iter.hasNext()) {
            ListEntry entry = (ListEntry) iter.next();
            outStr.append("[");
            Iterator iterColum = entry.getCustomElements().getTags().iterator();
            while (iterColum.hasNext()) {
                String tag = (String) iterColum.next();
                String value = entry.getCustomElements().getValue(tag);
                if (value != null) {
                    value = value.replaceAll("\\n", "\\\\n");
                    value = value.replaceAll("\"", "'");
                } else {
                    value = "";
                }
                outStr.append("\"").append(value).append("\"");
                if (iterColum.hasNext()) { // if not the last one, append a comma
                    outStr.append(",");
                }
            }
            outStr.append("]");
            if (iter.hasNext()) { // if not the last one, append a comma
                outStr.append(",\n");
            }
        }
        outStr.append("\n];");
        writeTextToFile(outStr.toString(), path + File.separator + storeName + ".js");
    }

    private List<File> getFileListing(File startDir) throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = startDir.listFiles();
        List<File> filesDirs = null;
        if (filesAndDirs != null) {
            filesDirs = Arrays.asList(filesAndDirs);
        }
        if (filesDirs != null) {
            for (File file : filesDirs) {
                if (file.getPath().contains(".html")) {
                    result.add(file);
                }
                if (!file.isFile()) { // if directory
                    List<File> deeperList = getFileListing(file);
                    result.addAll(deeperList);
                }
            }
        }
        return result;
    }
}
