package org.berlinvegan.generators;

public class Environment {
    public static final String BV_GOOGLE_USER_NAME = "BV_GOOGLE_USER_NAME";
    public static final String BV_GOOGLE_PASSWORD = "BV_GOOGLE_PASSWORD";
    
    public static String getGoogleUserName() throws Exception {
        final String username = System.getenv(BV_GOOGLE_USER_NAME);
        if (username == null) {
            throw new Exception("please set the env variable '" + BV_GOOGLE_USER_NAME + "'");
        }
        return username;
    }

    public static String getGooglePassword() throws Exception {
        final String password = System.getenv(BV_GOOGLE_PASSWORD);
        if (password == null) {
            throw new Exception("please set the env variable '" + BV_GOOGLE_PASSWORD + "'");
        }
        return password;
    }

}
