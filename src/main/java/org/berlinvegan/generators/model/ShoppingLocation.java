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
        final String value = elements.getValue("tagsbiodrogerieeisbackwarenkleidung");
        if (value == null) {
            throw new RuntimeException("Tags must be set.");
        }
        List<String> tags = Arrays.stream(value.split(",")).map(String::trim).collect(toList());
        List<String> allowedTags = Arrays.asList("Bio", "Backwaren", "Eis", "Drogerie", "Kleidung", "Naturkostladen");
        for (String tag : tags) {
            if (!allowedTags.contains(tag)) {
                throw new RuntimeException("Found illegal tag '" + tag + "'.");
            }
        }
        if (tags.size() < 1 || tags.size() > 6) {
            throw new RuntimeException("Found " + tags.size() + " tags. (expected 1 to 6)");
        }
        return tags.toArray(new String[0]);
    }
}
