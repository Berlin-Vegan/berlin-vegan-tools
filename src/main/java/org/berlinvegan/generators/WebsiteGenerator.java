package org.berlinvegan.generators;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.gdata.util.AuthenticationException;

/**
 * @author <a href="mailto:sandy.meier@inubit.com">Sandy Meier</a>
 * @since 7.0
 */
public class WebsiteGenerator extends Generator {
    public static final String USER_OPTION = "u";
    public static final String PASSWORD_OPTION = "p";
    public static final String OUTPUT_DIR_OPTION = "o";
    public static final String OUTPUT2_DIR_OPTION = "o2";
    public static final String WEBSITE_DE = "http://www.berlin-vegan.de";
    public static final String REVIEW_BASE_LOCATION_DE = "/berlin/restaurantkritiken/";
    public static final String REVIEW_BASE_LOCATION_DE_V2 = "/service/gastronomie/kritiken/";



    protected static String outputDir;
    protected static String outputDirV2;
    private static String userName;
    private static String password;

    public WebsiteGenerator() throws AuthenticationException {
        super(userName, password);
    }

    protected static void parseOptions(String[] args) {
        final CommandLineParser cmdLinePosixParser = new PosixParser();
        final Options posixOptions = constructOptions();
        CommandLine commandLine;
        try {
            commandLine = cmdLinePosixParser.parse(posixOptions,args);
            if (commandLine.hasOption(USER_OPTION)) {
                userName = commandLine.getOptionValue(USER_OPTION);
            }
            if (commandLine.hasOption(PASSWORD_OPTION)) {
                password = commandLine.getOptionValue(PASSWORD_OPTION);
            }
            if (commandLine.hasOption(OUTPUT_DIR_OPTION)) {
                outputDir = commandLine.getOptionValue(OUTPUT_DIR_OPTION);
            }
            if (commandLine.hasOption(OUTPUT2_DIR_OPTION)) {
                outputDirV2 = commandLine.getOptionValue(OUTPUT2_DIR_OPTION);
            }


        } catch (ParseException parseException){
            System.err.println(parseException.getMessage());
        }

    }

    public static Options constructOptions() {
        final Options options = new Options();
        options.addOption(USER_OPTION, true, "user name");
        options.addOption(PASSWORD_OPTION, true, "password");
        options.addOption(OUTPUT_DIR_OPTION, true, "output directory");
        options.addOption(OUTPUT2_DIR_OPTION, true, "output directory v2");
        return options;
    }
    public static void setOutputDir(String outputDir) {
        WebsiteGenerator.outputDir = outputDir;
    }
}
