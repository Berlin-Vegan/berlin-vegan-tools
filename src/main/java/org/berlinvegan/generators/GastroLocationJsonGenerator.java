package org.berlinvegan.generators;

import com.google.gson.Gson;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.GastroLocation;
import org.berlinvegan.generators.model.Picture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GastroLocationJsonGenerator extends WebsiteGenerator {

    private static final int TIMEOUT_MILLIS = 30 * 1000;
    private static final String CUSTOM_PICTURE_URL = "http://berlin-vegan.de/images/";

    public GastroLocationJsonGenerator() throws Exception {
        super();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 6) {  // 3 options with 1 value
            parseOptions(args);
            GastroLocationJsonGenerator generator = new GastroLocationJsonGenerator();
            generator.generate();
        } else {
            final HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("generate json", constructOptions(false, true));
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

        if (reviewURL != null && !reviewURL.isEmpty()) { // website
            Document doc = Jsoup.connect(REVIEW_DE_BASE_URL + reviewURL).timeout(TIMEOUT_MILLIS).get();
            String review = getLocationTextFromWebsite(doc);
            review = textEncode(review);
            review = hyphenate(review, LANG_DE);
            gastroLocation.setComment(review);
            final List<Picture> pictures = getLocationPicturesFromWebsite(doc);
            gastroLocation.setPictures(pictures);
        } else {// search in local folders
            final List<Picture> pictures = getLocationPicturesFromFolder(gastroLocation);
            gastroLocation.setPictures(pictures);
        }
    }

    private List<Picture> getLocationPicturesFromFolder(GastroLocation gastroLocation) {
        final ArrayList<Picture> pictures = new ArrayList<>();
        Path pictureFolder = Paths.get(inputImageDir).resolve(gastroLocation.getName());
        if (Files.isDirectory(pictureFolder)) {
            File[] files = new File(pictureFolder.toUri()).listFiles();
            if (files != null) {
                Arrays.sort(files);
                for (File file : files) {
                    Picture picture;
                    if (file.isFile()) {
                        picture = getPicture(gastroLocation, file);
                        if (picture != null) {
                            pictures.add(picture);
                        }
                    }
                }
            }
        }
        return pictures;
    }

    private Picture getPicture(GastroLocation gastroLocation, File file) {
        String imageURLStr = CUSTOM_PICTURE_URL + gastroLocation.getName() + "/" + file.getName();
        try {
            BufferedImage img = ImageIO.read(file);
            URL url = new URL(imageURLStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(),
                    url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            return new Picture(uri.toURL().toString(), img.getWidth(), img.getHeight());

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}

