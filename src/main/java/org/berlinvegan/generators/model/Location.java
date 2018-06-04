package org.berlinvegan.generators.model;

import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.berlinvegan.generators.Generator;
import org.berlinvegan.util.StringUtil;

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
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name must be set");
        }
        street = Generator.textEncode(elements.getValue("strasse"));
        if (StringUtils.isBlank(street)) {
            throw new IllegalArgumentException("street must be set");
        }
        cityCode = Integer.parseInt(elements.getValue("postleitzahl"));
        latCoord = Double.parseDouble(elements.getValue("lat"));
        longCoord = Double.parseDouble(elements.getValue("long"));
        telephone = elements.getValue("telefon");
        if ("".equals(telephone)) {
            throw new IllegalArgumentException("telephone must not be empty");
        }
        website = getUrl(elements, "website");
        if ("".equals(website)) {
            throw new IllegalArgumentException("website must not be empty");
        }
        otMon = getOpeningTime(elements, "mo");
        otTue = getOpeningTime(elements, "di");
        otWed = getOpeningTime(elements, "mi");
        otThu = getOpeningTime(elements, "do");
        otFri = getOpeningTime(elements, "fr");
        otSat = getOpeningTime(elements, "sa");
        otSun = getOpeningTime(elements, "so");
        vegan = Integer.parseInt(elements.getValue("veganfreundlich"));
        if (vegan != 2 && vegan != 4 && vegan != 5) {
            throw new IllegalArgumentException("vegan must be 2, 4 or 5");
        }
        comment = elements.getValue("kurzbeschreibungdeutsch");
        if ("".equals(comment)) {
            throw new IllegalArgumentException("comment must not be empty");
        }
        commentEnglish = elements.getValue("kurzbeschreibungenglisch");
        if ("".equals(commentEnglish)) {
            throw new IllegalArgumentException("commentEnglish must not be empty");
        }
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
        String simplifiedValue = StringUtil.removeEvenSpecialWhitespace(value); // Greatly simplifies regex.
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

    protected int getNumber(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value);
    }
    
    /**
     * @return 0 = false, 1 = true, -1 = unknown
     */
    protected int getTriStateBooleanAsInt(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        if (value == null) {
            return -1;
        }
        int i = Integer.parseInt(value);
        if (i != -1 && i != 0 && i != 1) {
            throw new RuntimeException("Found illegal tri-state boolean '" + value + "'.");
        }
        return i;
    }
    
    protected String getUrl(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        return (!StringUtils.isEmpty(value) && !value.startsWith("http")) ? "http://" + value : value;
    }
    
    protected String getTrimmedString(CustomElementCollection elements, String headerName) {
        final String value = elements.getValue(headerName);
        return value == null ? null : value.trim();
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
