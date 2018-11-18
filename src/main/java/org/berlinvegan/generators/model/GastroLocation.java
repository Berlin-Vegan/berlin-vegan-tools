package org.berlinvegan.generators.model;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

public class GastroLocation extends Location {
    public static final String OPEN_TIME_ONE_DAY = "<b>%s</b> %s<br/>";
    public static final String OPEN_TIME_MORE_DAYS = "<b>%s-%s</b> %s<br/>";
    public static final int TYPE_VEGAN = 5;
    private String district;
    private String publicTransport = "";
    private String email = "";
    private int handicappedAccessibleWc;
    private int dog;
    private int childChair;
    private int catering;
    private int seatsOutdoor;
    private int seatsIndoor;
    private int wlan;
    private int glutenFree;
    private int brunch;
    private int breakfast;
    private String[] tags;
    private List<String> districts;
    private List<Picture> pictures;

    public GastroLocation(String name, String district, double latCoord, double longCoord, int vegan) {
        this.name = name;
        this.district = district;
        this.latCoord = latCoord;
        this.longCoord = longCoord;
        this.vegan = vegan;
    }

    public GastroLocation(ListEntry entry) {
        super(entry);
        final CustomElementCollection elements = entry.getCustomElements();
        district = elements.getValue("stadtbezirk");
        if (StringUtils.isBlank(district)) {
            throw new IllegalArgumentException("district must be set");
        }
        publicTransport = elements.getValue("bvganfahrt");
        if (StringUtils.isBlank(publicTransport)) {
            throw new IllegalArgumentException("publicTransport must be set");
        }
        email = elements.getValue("e-mail");
        if ("".equals(email)) {
            throw new IllegalArgumentException("email must not be empty");
        }
        handicappedAccessibleWc = getTriStateBooleanAsInt(elements, "wcrollstuhlgeignet");
        dog = getTriStateBooleanAsInt(elements, "hunde");
        childChair = getTriStateBooleanAsInt(elements, "kindersitz");
        catering = getTriStateBooleanAsInt(elements, "catering");
        seatsIndoor = getNumber(elements, "sitzplaetzeinnen");
        seatsOutdoor = getNumber(elements, "sitzplaetzeaussen");
        tags = getTags(elements);
        wlan = getTriStateBooleanAsInt(elements, "wlan");
        glutenFree = getTriStateBooleanAsInt(elements, "glutenfrei");
    }

    private static String[] getTags(CustomElementCollection elements) {
        final String value = elements.getValue("tagsbittenurimbisscaferestaurantundeiscafeverwenden");
        if (value == null) {
            throw new RuntimeException("Tags must be set.");
        }
        List<String> tags =
                Arrays.stream(value.split(","))
                        .map(String::trim)
                        .map(tag -> tag.equals("Café") ? "Cafe" : tag)
                        .map(tag -> tag.equals("Eiscafé") ? "Eiscafe" : tag)
                        .collect(toList());
        List<String> allowedTags = Arrays.asList("Imbiss", "Cafe", "Restaurant", "Eiscafe");
        for (String tag : tags) {
            if (!allowedTags.contains(tag)) {
                throw new RuntimeException("Found illegal tag '" + tag + "'.");
            }
        }
        if (tags.size() < 1 || tags.size() > 4) {
            throw new RuntimeException("Found " + tags.size() + " tags. (expected 1 to 4)");
        }
        return tags.toArray(new String[0]);
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHandicappedAccessibleWc() {
        return handicappedAccessibleWc;
    }

    public void setHandicappedAccessibleWc(int handicappedAccessibleWc) {
        this.handicappedAccessibleWc = handicappedAccessibleWc;
    }

    public int getDog() {
        return dog;
    }

    public void setDog(int dog) {
        this.dog = dog;
    }

    public int getChildChair() {
        return childChair;
    }

    public void setChildChair(int childChair) {
        this.childChair = childChair;
    }

    public int getCatering() {
        return catering;
    }

    public void setCatering(int catering) {
        this.catering = catering;
    }

    public int getSeatsOutdoor() {
        return seatsOutdoor;
    }

    public void setSeatsOutdoor(int seatsOutdoor) {
        this.seatsOutdoor = seatsOutdoor;
    }

    public int getSeatsIndoor() {
        return seatsIndoor;
    }

    public void setSeatsIndoor(int seatsIndoor) {
        this.seatsIndoor = seatsIndoor;
    }

    public int getWlan() {
        return wlan;
    }

    public void setWlan(int wlan) {
        this.wlan = wlan;
    }

    public int getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(int glutenFree) {
        this.glutenFree = glutenFree;
    }

    public List<String> getDistricts() {
        return districts;
    }

    public void setDistricts(List<String> districts) {
        this.districts = districts;
    }

    public String getVeganHTML(String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));
        if (vegan == TYPE_VEGAN) {
            return bundle.getString("vegan");
        } else if (vegan == 4) {
            return bundle.getString("vegetarian");
        }
        return bundle.getString("omnivore");
    }

    public String getOpenTimesHTML(String language) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", new Locale(language));
        String[] weekdaysNames = new String[7];
        weekdaysNames[0] = bundle.getString("mon");
        weekdaysNames[1] = bundle.getString("tue");
        weekdaysNames[2] = bundle.getString("wed");
        weekdaysNames[3] = bundle.getString("thu");
        weekdaysNames[4] = bundle.getString("fri");
        weekdaysNames[5] = bundle.getString("sat");
        weekdaysNames[6] = bundle.getString("sun");

        String[] openTimes = new String[7];
        openTimes[0] = otMon != null ? otMon : ""; // if time is missing, init with empty string
        openTimes[1] = otTue != null ? otTue : "";
        openTimes[2] = otWed != null ? otWed : "";
        openTimes[3] = otThu != null ? otThu : "";
        openTimes[4] = otFri != null ? otFri : "";
        openTimes[5] = otSat != null ? otSat : "";
        openTimes[6] = otSun != null ? otSun : "";

        boolean openTimesAvailable = isOpenTimesAvailable(openTimes);

        if (openTimesAvailable) {
            return generateOpenTimesHTML(weekdaysNames, openTimes);
        }
        return "";
    }

    private String generateOpenTimesHTML(String[] weekdaysNames, String[] openTimes) {
        StringBuilder result = new StringBuilder();
        int equalStart = -1;
        for (int i = 0; i <= 6; i++) {
            if (i < 6 && openTimes[i].equalsIgnoreCase(openTimes[i + 1])) {
                // nachfolger identisch, mach weiter
                if (equalStart == -1) {
                    equalStart = i;
                }
            } else {
                if (equalStart == -1) {
                    // aktueller Tag ist einmalig
                    if (openTimes[i].isEmpty()) {
                        // geschlossen
                        String str = String.format(OPEN_TIME_ONE_DAY, weekdaysNames[i], "geschlossen");
                        result.append(str);
                    } else {
                        String str =
                                String.format(OPEN_TIME_ONE_DAY, weekdaysNames[i], openTimes[i] + " Uhr");
                        result.append(str);
                    }
                } else {
                    // es gibt zusammenhaengende Tage
                    String openTimesText =
                            openTimes[i].isEmpty() ? "geschlossen" : openTimes[i] + " Uhr";
                    String str =
                            String.format(
                                    OPEN_TIME_MORE_DAYS,
                                    weekdaysNames[equalStart],
                                    weekdaysNames[i],
                                    openTimesText);
                    result.append(str);
                    equalStart = -1;
                }
            }
        }
        return result.toString();
    }

    private boolean isOpenTimesAvailable(String[] openTimes) {
        return !openTimes[0].isEmpty()
                || !openTimes[1].isEmpty()
                || !openTimes[2].isEmpty()
                || !openTimes[3].isEmpty()
                || !openTimes[4].isEmpty()
                || !openTimes[5].isEmpty()
                || !openTimes[6].isEmpty();
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public int getBrunch() {
        return brunch;
    }

    public void setBrunch(int brunch) {
        this.brunch = brunch;
    }

    public int getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(int breakfast) {
        this.breakfast = breakfast;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", reviewURL='" + reviewURL + '\'' +
                ", street='" + street + '\'' +
                ", cityCode=" + cityCode +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", latCoord=" + latCoord +
                ", longCoord=" + longCoord +
                ", publicTransport='" + publicTransport + '\'' +
                ", telephone='" + telephone + '\'' +
                ", openComment='" + openComment + '\'' +
                ", website='" + website + '\'' +
                ", email='" + email + '\'' +
                ", otMon='" + otMon + '\'' +
                ", otTue='" + otTue + '\'' +
                ", otWed='" + otWed + '\'' +
                ", otThu='" + otThu + '\'' +
                ", otFri='" + otFri + '\'' +
                ", otSat='" + otSat + '\'' +
                ", otSun='" + otSun + '\'' +
                ", vegan=" + vegan +
                ", handicappedAccessible=" + handicappedAccessible +
                ", handicappedAccessibleWc=" + handicappedAccessibleWc +
                ", dog=" + dog +
                ", childChair=" + childChair +
                ", catering=" + catering +
                ", delivery=" + delivery +
                ", organic=" + organic +
                ", seatsOutdoor=" + seatsOutdoor +
                ", seatsIndoor=" + seatsIndoor +
                ", comment='" + comment + '\'' +
                ", commentEnglish='" + commentEnglish + '\'' +
                ", wlan=" + wlan +
                ", glutenFree=" + glutenFree +
                ", brunch=" + brunch +
                ", breakfast=" + breakfast +
                ", tags=" + Arrays.toString(tags) +
                ", districts=" + districts +
                ", pictures=" + pictures +
                '}';
    }
}
