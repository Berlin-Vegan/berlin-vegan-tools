package org.berlinvegan.util;

public final class StringUtil {
    
    private StringUtil() {
    }
    
    /**
     * Removes all whitespace from the given string, even characters that are not 
     * technically considered as whitespace, e.g., a non-breaking space.
     * 
     * See http://stackoverflow.com/questions/1060570/why-is-non-breaking-space-not-a-whitespace-character-in-java
     */
    public static String removeEvenSpecialWhitespace(String s) {
        final String noBreakSpace = "\u00A0";
        return s.replaceAll("\\s|" + noBreakSpace, "");
    }
}
