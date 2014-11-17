package org.berlinvegan.generators;


import java.util.*;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

public class Restaurant {
    public static final String OPEN_TIME_ONE_DAY = "<b>%s</b> %s<br/>";
    public static final String OPEN_TIME_MORE_DAYS = "<b>%s-%s</b> %s<br/>";

    private String name;
    private String reviewURL = "";
    private String street = "";
    private int cityCode;
    private String district = "";
    private String latCoord;
    private String longCoord;
    private String bvg = "";
    private String telephone = "";
    private String openComment = "";
    private String website = "";
    private String email = "";
    private String otMon = ""; // open times monday
    private String otTue = "";
    private String otWed = "";
    private String otThu = "";
    private String otFri = "";
    private String otSat = "";
    private String otSun = "";
    private int vegan;
    private int handicappedAccessible;
    private int handicappedAccessibleWc;
    private int dog;
    private int childChair;
    private int catering;
    private int delivery;
    private int organic;
    private int seatsOutdoor;
    private int seatsIndoor;
    private String comment;
    private int wlan;
    private int glutenFree;
    private String[] tags;

    private Rating rating;
    private List<String> districts;

    public Restaurant(String name, String district, String latCoord, String longCoord, int vegan) {
        this.name = name;
        this.district = district;
        this.latCoord = latCoord;
        this.longCoord = longCoord;
        this.vegan = vegan;
    }

    public Restaurant(ListEntry entry) {
        final CustomElementCollection elements = entry.getCustomElements();
        //printColumnHeaderNames(elements);
        name = elements.getValue("name");
        reviewURL = elements.getValue("reviewurl");
        street = Generator.textEncode(elements.getValue("strasse"));
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
        handicappedAccessible = getIntValue(elements, "restaurantrollstuhlgeeignet");
        handicappedAccessibleWc = getIntValue(elements, "wcrollstuhlgeignet");
        dog = getIntValue(elements, "hunde");
        childChair = getIntValue(elements, "kindersitz");
        catering = getIntValue(elements, "catering");
        delivery = getIntValue(elements, "lieferservice");
        organic = getIntValue(elements, "bio");
        seatsIndoor = getIntValue(elements, "sitzplaetzeinnen");
        seatsOutdoor = getIntValue(elements, "sitzplaetzeaussen");
        comment = elements.getValue("zusaetzlicherkommentarfuerkarte");
        final String tagStr = elements.getValue("tagsbittenurimbisscaferestaurantundeiscafeverwenden");
        if (tagStr != null) {
            tags = tagStr.split(",");
        }
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
        if (value.equals("0")) {
            return false;
        } else if (value.equals("1")) {
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

    public int getHandicappedAccessible() {
        return handicappedAccessible;
    }

    public void setHandicappedAccessible(int handicappedAccessible) {
        this.handicappedAccessible = handicappedAccessible;
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
        
        boolean openTimesAvailable = 
            !openTimes[0].isEmpty() 
            || !openTimes[1].isEmpty() 
            || !openTimes[2].isEmpty() 
            || !openTimes[3].isEmpty() 
            || !openTimes[4].isEmpty() 
            || !openTimes[5].isEmpty() 
            || !openTimes[6].isEmpty();
        
        if (openTimesAvailable) {
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
            ", ha=" + handicappedAccessible +
            ", ha_wc=" + handicappedAccessibleWc +
            ", dog=" + dog +
            ", childChair=" + childChair +
            ", catering=" + catering +
            ", delivery=" + delivery +
            ", organic=" + organic +
            ", seat_out=" + seatsOutdoor +
            ", seat_in=" + seatsIndoor +
            ", comment='" + comment + '\'' +
            ", wlan=" + wlan +
            ", glutenFree=" + glutenFree +
            ", tags=" + (tags == null ? null : Arrays.asList(tags)) +
            '}';
    }
}
