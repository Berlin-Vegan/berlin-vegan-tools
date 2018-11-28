package org.berlinvegan.generators.model;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;

public class ShoppingLocation extends Location {
    private String[] tags;

    public ShoppingLocation() {
    }

    public ShoppingLocation(ListEntry entry) {
        super(entry);
        final CustomElementCollection elements = entry.getCustomElements();
        tags = getTags(elements);
    }
    
    private static String[] getTags(CustomElementCollection elements) {
        final String value = elements.getValue("tags");
        if (value == null) {
            throw new RuntimeException("Tags must be set.");
        }
        List<String> tags =
            Arrays.stream(value.split(","))
                .map(String::trim)
                .map(tag -> {
                    switch (tag) {
                        case "food": return "foods";
                        case "fashion": return "clothing";
                        case "cosmetics": return "toiletries";
                        default: return tag;
                    }
                })
                .collect(toList());
        List<String> allowedTags =
            Arrays.asList(
                "foods",
                "clothing",
                "toiletries",
                "supermarket",
                "hairdressers",
                "fitness",
                "tattoostudio",
                "accommodation"
            );
        for (String tag : tags) {
            if (!allowedTags.contains(tag)) {
                throw new RuntimeException("Found illegal tag '" + tag + "'.");
            }
        }
        if (tags.size() < 1 || tags.size() > 8) {
            throw new RuntimeException("Found " + tags.size() + " tags. (expected 1 to 4)");
        }
        return tags.toArray(new String[0]);
    }
}
