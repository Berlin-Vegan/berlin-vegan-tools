package org.berlinvegan.generators;

import net.davidashen.text.Hyphenator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.gdata.util.AuthenticationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * @author <a href="mailto:smeier@tapnic.com">Sandy Meier</a>
 */
public class WebsiteGenerator extends Generator {
    public static final String USER_OPTION = "u";
    public static final String PASSWORD_OPTION = "p";
    public static final String OUTPUT_DIR_OPTION = "o";
    public static final String WEBSITE_DE = "http://www.berlin-vegan.de";
    public static final String REVIEW_BASE_LOCATION_DE = "/essen-und-trinken/kritiken/";
    public static final String REVIEW_DE_BASE_URL = 
        "http://www.berlin-vegan.de/essen-und-trinken/kritiken/";
    protected static String outputDir;
    private static String userName;
    private static String password;

    public WebsiteGenerator() throws AuthenticationException {
        super(userName, password);
    }

    protected static void parseOptions(String[] args) throws Exception {
        final CommandLineParser cmdLinePosixParser = new PosixParser();
        final Options posixOptions = constructOptions(true);
        CommandLine commandLine;
        try {
            commandLine = cmdLinePosixParser.parse(posixOptions, args);
            if (commandLine.hasOption(USER_OPTION)) {
                userName = commandLine.getOptionValue(USER_OPTION);
            }
            if (commandLine.hasOption(PASSWORD_OPTION)) {
                password = commandLine.getOptionValue(PASSWORD_OPTION);
            }
            if (commandLine.hasOption(OUTPUT_DIR_OPTION)) {
                outputDir = commandLine.getOptionValue(OUTPUT_DIR_OPTION);
            }

            // if username,password is not set on commandline, try to read from env vars
            if (userName == null) {
                userName = Environment.getGoogleUserName();
            }
            if (password == null) {
                password = Environment.getGooglePassword();
            }

        } catch (ParseException parseException) {
            System.err.println(parseException.getMessage());
        }

    }
    public static Options constructOptions() {
        return constructOptions(true);
    }
    public static Options constructOptions(boolean withOutputDir) {
        final Options options = new Options();
        options.addOption(USER_OPTION, true, "user name");
        options.addOption(PASSWORD_OPTION, true, "password");
        if (withOutputDir) {
            options.addOption(OUTPUT_DIR_OPTION, true, "output directory");
        }
        return options;
    }
    
    public static void setOutputDir(String outputDir) {
        WebsiteGenerator.outputDir = outputDir;
    }

    protected OutputStreamWriter getUTF8Writer(String filePath) throws FileNotFoundException {
        final File file = new File(filePath);
        final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        return new OutputStreamWriter(new FileOutputStream(file), encoder);
    }
    
    protected String hyphenate(String text, String language) throws IOException {
        Hyphenator h = new Hyphenator();
        
        String resourceName = 
            "org/berlinvegan/generators/" + (language.equals(LANG_DE) ? "dehyphx.tex" : "hyphen.tex");
        
        InputStream fileInputStream = this.getClass().getClassLoader().getResourceAsStream(resourceName);
        h.loadTable(fileInputStream);

        text = h.hyphenate(text, 4, 3);
        text = text.replaceAll("\u00ad", "&shy;");
        return text;
    }
}
