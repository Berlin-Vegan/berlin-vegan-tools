package org.berlinvegan.generators;


import java.util.*;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

public class Restaurant {
    public static final String OPEN_TIME_ONE_DAY = "<b>%s</b> %s<br/>";
    public static final String OPEN_TIME_MORE_DAYS = "<b>%s-%s</b> %s<br/>";

    String name;
    String reviewURL;
    String street;
    int cityCode;
    String district;
    String latCoord;
    String longCoord;
    String bvg;
    String telephone;
    String openComment;
    String website;
    String email;
    String otMon; // open times monday
    String otTue;
    String otWed;
    String otThu;
    String otFri;
    String otSat;
    String otSun;
    int vegan;
    int ha; // handicapped accessible
    int ha_wc;
    int dog;
    int childChair;
    int catering;
    int delivery;
    int organic;
    int seat_out;
    int seat_in;
    String comment;
    int wlan;
    int glutenFree;
    private final String[] tags;

    private Rating rating;
    private ArrayList<String> districts;

    public Restaurant(ListEntry entry) {
        final CustomElementCollection elements = entry.getCustomElements();
        //printColumnHeaderNames(elements);
        name = elements.getValue("name");
        reviewURL = elements.getValue("reviewurl");
        street = elements.getValue("strasse");
        cityCode = Integer.parseInt(elements.getValue("postleitzahl"));
        district = elements.getValue("stadtbezirk");
        latCoord = elements.getValue("lat");
        longCoord = elements.getValue("long");
        bvg = elements.getValue("bvganfahrt");
        telephone = elements.getValue("telefon");
        otMon = getStringValue(elements, "mo");
        otTue = getStringValue(elements, "di");
        otWed = getStringValue(elements, "mi");
        otThu = getStringValue(elements, "do");
        otFri = getStringValue(elements, "fr");
        otSat = getStringValue(elements, "sa");
        otSun = getStringValue(elements, "so");
        openComment = elements.getValue("oeffnungszeitenkommentar");
        website = elements.getValue("website");
        email = elements.getValue("e-mail");
        vegan = Integer.parseInt(elements.getValue("veganfreundlich"));
        ha = getIntValue(elements, "restaurantrollstuhlgeeignet");
        ha_wc = getIntValue(elements, "wcrollstuhlgeignet");
        dog = getIntValue(elements, "hunde");
        childChair = getIntValue(elements, "kindersitz");
        catering = getIntValue(elements, "catering");
        delivery = getIntValue(elements, "lieferservice");
        organic = getIntValue(elements, "bio");
        seat_in = getIntValue(elements, "sitzplaetzeinnen");
        seat_out = getIntValue(elements, "sitzplaetzeaussen");
        comment = elements.getValue("zusaetzlicherkommentarfuerkarte");
        final String tagStr = elements.getValue("tagsbittenurimbisscaferestaurantundeiscafeverwenden");
        tags = tagStr.split(",");
        wlan = getIntValue(elements, "wlan");
        glutenFree = getIntValue(elements, "glutenfrei");
        rating = new Rating(0, 0);


    }

    private void printColumnHeaderNames(CustomElementCollection elements) {
        final Set<String> columnHeaderNames = elements.getTags();
        for (String tag : columnHeaderNames) {
            System.out.println(columnHeaderNames.size() + ": " + tag);
        }
    }

    /**
     * @param elements
     * @param headerName
     * @return 0 = false, 1 = true, null = unknow
     */
    private Boolean getBooleanValue(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (value == null) {
            return null;
        }
        switch (value) {
            case "0":
                return false;
            case "1":
                return true;

        }
        return null;
    }

    private String getStringValue(CustomElementCollection elements, String headerName) {
        String value = elements.getValue(headerName);
        if (value == null) {
            value = "";
        }
        return value.replaceAll("[\r\n]+", "");

    }

    /**
     * @param elements
     * @param headerName
     * @return 0 = false, 1 = true, -1 = unknow
     */
    private int getIntValue(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatCoord() {
        return latCoord;
    }

    public void setLatCoord(String latCoord) {
        this.latCoord = latCoord;
    }

    public String getLongCoord() {
        return longCoord;
    }

    public void setLongCoord(String longCoord) {
        this.longCoord = longCoord;
    }

    public String getBvg() {
        return bvg;
    }

    public void setBvg(String bvg) {
        this.bvg = bvg;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpenComment() {
        return openComment;
    }

    public void setOpenComment(String openComment) {
        this.openComment = openComment;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtMon() {
        return otMon;
    }

    public void setOtMon(String otMon) {
        this.otMon = otMon;
    }

    public String getOtTue() {
        return otTue;
    }

    public void setOtTue(String otTue) {
        this.otTue = otTue;
    }

    public String getOtWed() {
        return otWed;
    }

    public void setOtWed(String otWed) {
        this.otWed = otWed;
    }

    public String getOtThu() {
        return otThu;
    }

    public void setOtThu(String otThu) {
        this.otThu = otThu;
    }

    public String getOtFri() {
        return otFri;
    }

    public void setOtFri(String otFri) {
        this.otFri = otFri;
    }

    public String getOtSat() {
        return otSat;
    }

    public void setOtSat(String otSat) {
        this.otSat = otSat;
    }

    public String getOtSun() {
        return otSun;
    }

    public void setOtSun(String otSun) {
        this.otSun = otSun;
    }

    public int getVegan() {
        return vegan;
    }

    public void setVegan(int vegan) {
        this.vegan = vegan;
    }

    public int getHa() {
        return ha;
    }

    public void setHa(int ha) {
        this.ha = ha;
    }

    public int getHa_wc() {
        return ha_wc;
    }

    public void setHa_wc(int ha_wc) {
        this.ha_wc = ha_wc;
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

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public int getOrganic() {
        return organic;
    }

    public void setOrganic(int organic) {
        this.organic = organic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getSeat_out() {
        return seat_out;
    }

    public void setSeat_out(int seat_out) {
        this.seat_out = seat_out;
    }

    public int getSeat_in() {
        return seat_in;
    }

    public void setSeat_in(int seat_in) {
        this.seat_in = seat_in;
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



    public ArrayList<String> getDistricts() {
        return districts;
    }

    public void setDistricts(ArrayList<String> districts) {
        this.districts = districts;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
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
        openTimes[0] = otMon;
        openTimes[1] = otTue;
        openTimes[2] = otWed;
        openTimes[3] = otThu;
        openTimes[4] = otFri;
        openTimes[5] = otSat;
        openTimes[6] = otSun;
        StringBuilder result = new StringBuilder();
        if (!openTimes[0].isEmpty() || !openTimes[1].isEmpty() || !openTimes[2].isEmpty() || !openTimes[3].isEmpty() || !openTimes[4].isEmpty() || !openTimes[5]
            .isEmpty() || !openTimes[6].isEmpty()) {
            int equalStart = -1;
            for (int i = 0; i <= 6; i++) {
                if (i<6 && openTimes[i].equalsIgnoreCase(openTimes[i + 1])) { // nachfolger identisch, mach weiter
                    if (equalStart == -1) {
                        equalStart = i;
                    }
                } else {
                    if (equalStart == -1) { // aktueller    Tag ist einmalig
                        if (openTimes[i].isEmpty()) { // geschlossen
                            String str = String.format(OPEN_TIME_ONE_DAY, weekdaysNames[i], "geschlossen");
                            result.append(str);
                        } else {
                            String str = String.format(OPEN_TIME_ONE_DAY, weekdaysNames[i], openTimes[i] + " Uhr");
                            result.append(str);
                        }
                    } else { // es gibt zusammenh�ngende Tage
                        if (openTimes[i].isEmpty()) {
                            String str = String.format(OPEN_TIME_MORE_DAYS, weekdaysNames[equalStart], weekdaysNames[i], "geschlossen");
                            result.append(str);
                        } else {
                            String str = String.format(OPEN_TIME_MORE_DAYS, weekdaysNames[equalStart], weekdaysNames[i], openTimes[i] + " Uhr");
                            result.append(str);
                        }
                        equalStart = -1;
                    }
                }
            }

        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "Restaurant{" +
            "name='" + name + '\'' +
            ", reviewURL='" + reviewURL + '\'' +
            ", street='" + street + '\'' +
            ", cityCode=" + cityCode +
            ", district='" + district + '\'' +
            ", latCoord='" + latCoord + '\'' +
            ", longCoord='" + longCoord + '\'' +
            ", bvg='" + bvg + '\'' +
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
            ", ha=" + ha +
            ", ha_wc=" + ha_wc +
            ", dog=" + dog +
            ", childChair=" + childChair +
            ", catering=" + catering +
            ", delivery=" + delivery +
            ", organic=" + organic +
            ", seat_out=" + seat_out +
            ", seat_in=" + seat_in +
            ", comment='" + comment + '\'' +
            ", wlan=" + wlan +
            ", glutenFree=" + glutenFree +
            ", tags=" + (tags == null ? null : Arrays.asList(tags)) +
            '}';
    }
}
