package org.berlinvegan.generators.model;

import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import org.berlinvegan.generators.model.Location;


public class ShoppingLocation extends Location {
    private String[] tags;
    public ShoppingLocation() {
    }

    public ShoppingLocation(ListEntry entry) {
        super(entry);
        final CustomElementCollection elements = entry.getCustomElements();
        final String tagStr = elements.getValue("tagsbiodrogerieeisbackwarenkleidung");
        if (tagStr != null) {
            tags = tagStr.split(",");
        }

    }
}
