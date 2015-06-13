package org.berlinvegan.generators;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 22.07.12
 * Time: 19:49
 */
public class Generator {
    public static final String LANG_DE = "de";
    public static final String LANG_EN = "en";
    public static final String TABLE_RESTAURANTS = "Restaurants";
    public static final String TABLE_SHOPPING = "Shopping";
    public static final String TABLE_BACKWAREN = "Backwaren";
    public static final String TABLE_BIO_REFORM = "BioReform";
    public static final String TABLE_CAFES = "Cafes";
    protected SpreadsheetService service;
    protected FeedURLFactory factory;

    public Generator(String username, String password) throws AuthenticationException {
        if (username != null) {
            factory = FeedURLFactory.getDefault();
            service = new SpreadsheetService("generator");
            service.setUserCredentials(username, password);
        }

    }

    public List<SpreadsheetEntry> getSpreadsheetEntries() throws Exception {
        SpreadsheetFeed feed = service.getFeed(
                factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
        return feed.getEntries();
    }

    protected ListFeed getFeed(URL listFeedUrl) throws IOException, ServiceException {
        return service.getFeed(listFeedUrl, ListFeed.class);
    }

    @SuppressWarnings("checkstyle:indentation")
    public List<ListEntry> addEntries(List<ListEntry> entries, SpreadsheetEntry spreadsheet)
            throws IOException, ServiceException {

        URL listFeedUrl = spreadsheet.getDefaultWorksheet().getListFeedUrl();
        ListFeed feed = getFeed(listFeedUrl);
        if (entries == null) {
            entries = feed.getEntries();
        } else {
            entries.addAll(feed.getEntries());
        }
        return entries;
    }

    /* search the Berlin Vegan Google Docs for Table "Restaurant", download and parse the entries*/
    public List<Restaurant> getRestaurantsFromServer() throws Exception {
        final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        final List<SpreadsheetEntry> spreadsheetEntries = getSpreadsheetEntries();
        for (SpreadsheetEntry entry : spreadsheetEntries) {
            if (entry.getTitle().getPlainText().equals(Generator.TABLE_RESTAURANTS)) {
                List<ListEntry> entryList = addEntries(null, entry);
                for (ListEntry listEntry : entryList) {
                    final Restaurant restaurant = new Restaurant(listEntry);
                    restaurants.add(restaurant);
                }
            }
        }
        // init districts, a restaurant with more then one filiale is located in several districts
        for (Restaurant restaurant : restaurants) {
            ArrayList<String> districts = new ArrayList<String>();
            String reviewURL = restaurant.getReviewURL();
            if (reviewURL != null) {
                for (Restaurant rest : restaurants) {
                    if (reviewURL.equalsIgnoreCase(rest.getReviewURL())
                            && !districts.contains(rest.getDistrict())) {
                        districts.add(rest.getDistrict());
                    }
                }
                restaurant.setDistricts(districts);
            }
        }

        return restaurants;
    }

    protected void writeTextToFile(String text, String filePath) throws IOException {
//        Writer output = null;
        File file = new File(filePath);
        FileUtils.write(file, text, "UTF-8");
//        final FileWriter fileWriter = new FileWriter(file);
//        output = new BufferedWriter(fileWriter);
//        output.write(text);
//        output.close();
    }

    public static String textEncode(String text) {
        text = text.replaceAll("\"", "\\\\\"");
        text = text.replaceAll("\n", "");
        text = text.replaceAll("\r", "");
        text = text.replace("&", "&amp;");
        return text;
    }

    protected String getLocationTextFromWebsite(Document document) throws IOException {
        String text = "";
        Elements textElements = document.select("div.entry-content > p");
        for (Element textElement : textElements) {
            text += textElement.text() + "<br/><br/>";
        }
        return text;
    }

    protected List<Picture> getLocationPicturesFromWebsite(Document document) {
        final ArrayList<Picture> pictures = new ArrayList<>();
        final Elements elements = document.select("div > a > img");
        for (Element element : elements) {
            final Picture picture = parsePicture(element);
            if (picture != null) {
                pictures.add(picture);
            }
        }
        return pictures;
    }

    private Picture parsePicture(Element element) {
        final String url = element.attr("data-orig-file");
        if (isPicture(url)) {
            final String sizeString = element.attr("data-orig-size");
            if (StringUtils.isNotEmpty(sizeString)) {
                final String[] sizeArray = sizeString.split(",");
                int width = Integer.parseInt(sizeArray[0]);
                int height = Integer.parseInt(sizeArray[1]);
                return new Picture(url, width, height);

            }
        }
        return null;
    }

    private boolean isPicture(String url) {
        return StringUtils.endsWithIgnoreCase(url, "jpg");
    }


}
