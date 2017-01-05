package org.berlinvegan.generators.model;

import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.Generator;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

public class Location {
    protected String id;
    protected String name;
    protected String street = "";
    protected int cityCode;
    protected String city = "Berlin";
    protected double latCoord;
    protected double longCoord;
    protected String telephone = "";
    protected String website = "";
    protected String otMon = ""; // open times monday
    protected String otTue = "";
    protected String otWed = "";
    protected String otThu = "";
    protected String otFri = "";
    protected String otSat = "";
    protected String otSun = "";
    protected int vegan;
    protected String comment;
    protected String commentEnglish;

    public Location() {
    }

    public Location(ListEntry entry) {
        final CustomElementCollection elements = entry.getCustomElements();
        name = elements.getValue("name");
        if (name == null) {
            throw new IllegalArgumentException("name must be set");
        }
        street = Generator.textEncode(elements.getValue("strasse"));
        cityCode = Integer.parseInt(elements.getValue("postleitzahl"));
        latCoord = Double.parseDouble(elements.getValue("lat"));
        longCoord = Double.parseDouble(elements.getValue("long"));
        telephone = elements.getValue("telefon");
        otMon = getOpeningTime(elements, "mo");
        otTue = getOpeningTime(elements, "di");
        otWed = getOpeningTime(elements, "mi");
        otThu = getOpeningTime(elements, "do");
        otFri = getOpeningTime(elements, "fr");
        otSat = getOpeningTime(elements, "sa");
        otSun = getOpeningTime(elements, "so");
        website = getUrl(elements, "website");
        vegan = Integer.parseInt(elements.getValue("veganfreundlich"));
        comment = elements.getValue("kurzbeschreibungdeutsch");
        commentEnglish = elements.getValue("kurzbeschreibungenglisch");
        // "generate unique id
        id = generateId();
    }

    protected String generateId() {
        final String idStr = name + latCoord + longCoord;
        return DigestUtils.md5Hex(idStr);
    }

    private static String getOpeningTime(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (StringUtils.isBlank(value)) {
            return "";
        }
        String simplifiedValue = removeEvenSpecialWhitespace(value); // Greatly simplifies regex.
        final String regex = "\\d{1,2}(\\:\\d{2})?\\-(\\d{1,2}(\\:\\d{2})?)?";
        if (!Pattern.matches(regex, simplifiedValue)) {
            throw new RuntimeException("Found illegal opening time '" + value + "'.");
        }
        if (simplifiedValue.endsWith("-")) {
            simplifiedValue += "0";
        } else if (simplifiedValue.endsWith("-24")) {
            simplifiedValue = simplifiedValue.replaceAll("-24", "-0");
        }
        return simplifiedValue.replaceAll("-", " - "); // Cause we removed the spaces when simplifying.
    }
    
    private static String removeEvenSpecialWhitespace(String s) {
        final String noBreakSpace = "\u00A0";
        return s.replaceAll("\\s|" + noBreakSpace, "");
    }

    /**
     * @return 0 = false, 1 = true, -1 = unknown
     */
    protected int getIntValue(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }
    
    protected String getUrl(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        return (!StringUtils.isEmpty(value) && !value.startsWith("http")) ? "http://" + value : value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public double getLatCoord() {
        return latCoord;
    }

    public void setLatCoord(double latCoord) {
        this.latCoord = latCoord;
    }

    public double getLongCoord() {
        return longCoord;
    }

    public void setLongCoord(double longCoord) {
        this.longCoord = longCoord;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentEnglish() {
        return commentEnglish;
    }

    public void setCommentEnglish(String commentEnglish) {
        this.commentEnglish = commentEnglish;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
