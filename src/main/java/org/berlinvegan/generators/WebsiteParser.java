package org.berlinvegan.generators;

import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.model.Picture;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public final class WebsiteParser {
    private WebsiteParser() {
    }
    public static List<Picture> getLocationPicturesFromWebsite(Document document) {
        final ArrayList<Picture> pictures = new ArrayList<>();
        pictures.addAll(getLocationPicturesFromWebsiteLegacy(document)); // old WP style
        pictures.addAll(getLocationPicturesFromWebsiteBlockBased(document)); // new block based WP style
        return pictures;
    }

    private static List<Picture> getLocationPicturesFromWebsiteBlockBased(Document document) {
        final ArrayList<Picture> pictures = new ArrayList<>();
        final Elements elements = document.select("figure > a > img , figure > img");
        for (Element element : elements) {
            final Picture picture = parsePictureBlockBased(element);
            if (picture != null) {
                pictures.add(picture);
            }
        }
        return pictures;
    }

    private static Picture parsePictureBlockBased(Element element) {
        final String src = element.attr("src");
        if (isPicture(src)) {
            try {
                URL url = new URL(src);
                URLConnection conn = url.openConnection();
                InputStream in = conn.getInputStream();
                BufferedImage image = ImageIO.read(in);
                int width = image.getWidth();
                int height = image.getHeight();
                return new Picture(src, width, height);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static List<Picture> getLocationPicturesFromWebsiteLegacy(Document document) {
        final ArrayList<Picture> pictures = new ArrayList<>();
        final Elements elements = document.select("div > a > img");
        for (Element element : elements) {
            final Picture picture = parsePictureLegacy(element);
            if (picture != null) {
                pictures.add(picture);
            }
        }
        return pictures;
    }

    private static Picture parsePictureLegacy(Element element) {
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

    private static boolean isPicture(String url) {
        return StringUtils.endsWithIgnoreCase(url, "jpg");
    }
}
