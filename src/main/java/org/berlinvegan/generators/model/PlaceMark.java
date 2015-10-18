package org.berlinvegan.generators.model;

public class PlaceMark {
    private String name;
    private String description;
    private String latPos;
    private String longPos;

    public PlaceMark(String name, String description, String latPos, String longPos) {
        this.name = name;
        this.description = description;
        this.latPos = latPos;
        this.longPos = longPos;
    }
    
    public String toKMLFormat() {
        final StringBuilder builder = new StringBuilder();
        builder.append("<Placemark>\n");
        builder.append("<name>").append(name).append("</name>\n");
        builder.append("<description>").append(description).append("</description>\n");
        builder.append("<Point><coordinates>");
        builder.append(longPos).append(",").append(latPos).append(",0");
        builder.append("</coordinates></Point>\n");
        builder.append("</Placemark>\n");
        return builder.toString();
    }
}
