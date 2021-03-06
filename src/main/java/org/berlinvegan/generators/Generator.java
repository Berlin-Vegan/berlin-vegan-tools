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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.berlinvegan.generators.model.GastroLocation;
import org.berlinvegan.generators.model.ShoppingLocation;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    // bv django backend
    private static final String BV_DATA_URL = "https://data.berlin-vegan.de/api/GastroLocations.json";

    private SpreadsheetService service;
    private FeedURLFactory factory;

    Generator(String refreshToken, String clientSecret) throws Exception {
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

    private ListFeed getFeed(URL listFeedUrl) throws IOException, ServiceException {
        return service.getFeed(listFeedUrl, ListFeed.class);
    }

    List<ListEntry> addEntries(List<ListEntry> entries, SpreadsheetEntry spreadsheet)
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

    List<GastroLocation> getGastroLocationDataFromServer() throws Exception {
        return getGastroLocationDataFromServer(false);
    }

    /**
     * search the Berlin Vegan Google Docs for Table "Restaurant", download and parse the entries
     *
     * @param fromGoogle if true, load from google docs, if false load from BV django app
     * @return list of locations
     * @throws Exception
     */
    List<GastroLocation> getGastroLocationDataFromServer(boolean fromGoogle) throws Exception {
        ArrayList<GastroLocation> gastroLocations;
        if (fromGoogle) {
            gastroLocations = getGastroLocationDataFromGoogleServer();
        } else {
            gastroLocations = getGastroLocationDataFromBVServer();
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

    private ArrayList<GastroLocation> getGastroLocationDataFromBVServer() throws IOException {
        URL url = new URL(BV_DATA_URL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonElement root = new JsonParser().parse(new InputStreamReader((InputStream) request.getContent()));
        Type collectionType = new TypeToken<ArrayList<GastroLocation>>() {}.getType();
        return new Gson().fromJson(root, collectionType);
    }


    private ArrayList<GastroLocation> getGastroLocationDataFromGoogleServer() throws Exception {
        final ArrayList<GastroLocation> gastroLocations = new ArrayList<GastroLocation>();
        final List<SpreadsheetEntry> spreadsheetEntries = getSpreadsheetEntries();
        for (SpreadsheetEntry entry : spreadsheetEntries) {
            if (entry.getTitle().getPlainText().equals(Generator.TABLE_RESTAURANTS)) {
                List<ListEntry> entryList = addEntries(null, entry);
                GastroLocation gastroLocation = null;
                for (ListEntry listEntry : entryList) {
                    try {
                        gastroLocation = new GastroLocation(listEntry);
                        gastroLocations.add(gastroLocation);
                    } catch (Exception ex) {
                        if (gastroLocation != null) {
                            System.err.println("Failed to parse Gastrolocation after: " + gastroLocation.getName());
                        }
                        throw ex;
                    }
                }
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

    protected String getLocationTextFromWebsite(Document document) {
        String text = "";
        Elements textElements = document.select("div.entry-content > p");
        for (Element textElement : textElements) {
            text += textElement.text() + "<br/><br/>";
        }
        return text;
    }
}
