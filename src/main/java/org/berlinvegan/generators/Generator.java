package org.berlinvegan.generators;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;
import org.berlinvegan.generators.model.Picture;
import org.berlinvegan.generators.model.ShoppingLocation;
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
    public static final String TABLE_SHOPPING = "Einkaufen";

    // old tables, used only for old apps (phonegap)
    public static final String TABLE_SHOPPING_OLD = "Shopping";
    public static final String TABLE_BACKWAREN = "Backwaren";
    public static final String TABLE_BIO_REFORM = "BioReform";
    public static final String TABLE_CAFES = "Cafes";
    public static final String CLIENT_ID = "163232640652-b4m9qk4crvc2ck1pug6eqlapt0b64ncp.apps.googleusercontent.com";
    protected SpreadsheetService service;
    protected FeedURLFactory factory;

    public Generator(String refreshToken, String clientSecret) throws Exception {
        if (refreshToken != null) {
            factory = FeedURLFactory.getDefault();
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            JacksonFactory jsonFactory = new JacksonFactory();
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(CLIENT_ID, clientSecret)
                    .build();

            credential.setRefreshToken(refreshToken);
            credential.refreshToken();
            service = new SpreadsheetService("generator");
            service.setOAuth2Credentials(credential);
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
    public List<GastroLocation> getGastroLocationFromServer() throws Exception {
        final ArrayList<GastroLocation> gastroLocations = new ArrayList<GastroLocation>();
        final List<SpreadsheetEntry> spreadsheetEntries = getSpreadsheetEntries();
        for (SpreadsheetEntry entry : spreadsheetEntries) {
            if (entry.getTitle().getPlainText().equals(Generator.TABLE_RESTAURANTS)) {
                List<ListEntry> entryList = addEntries(null, entry);
                for (ListEntry listEntry : entryList) {
                    final GastroLocation gastroLocation = new GastroLocation(listEntry);
                    gastroLocations.add(gastroLocation);
                }
            }
        }
        // init districts, a restaurant with more then one filiale is located in several districts
        for (GastroLocation gastroLocation : gastroLocations) {
            ArrayList<String> districts = new ArrayList<String>();
            String reviewURL = gastroLocation.getReviewURL();
            if (reviewURL != null) {
                for (GastroLocation rest : gastroLocations) {
                    if (reviewURL.equalsIgnoreCase(rest.getReviewURL())
                            && !districts.contains(rest.getDistrict())) {
                        districts.add(rest.getDistrict());
                    }
                }
                gastroLocation.setDistricts(districts);
            }
        }

        return gastroLocations;
    }
    protected List<ShoppingLocation> getShoppingLocationFromServer() throws Exception {
        final ArrayList<ShoppingLocation> locations = new ArrayList<ShoppingLocation>();
        final List<SpreadsheetEntry> spreadsheetEntries = getSpreadsheetEntries();
        for (SpreadsheetEntry entry : spreadsheetEntries) {
            if (entry.getTitle().getPlainText().equals(Generator.TABLE_SHOPPING)) {
                List<ListEntry> entryList = addEntries(null, entry);
                for (ListEntry listEntry : entryList) {
                    final ShoppingLocation location = new ShoppingLocation(listEntry);
                    locations.add(location);
                }
            }
        }
        return locations;
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
