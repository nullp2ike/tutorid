package utils;

import java.util.Properties;

public class Setup {

    public static String apiUrl;
    public static String usersUrl;
    public static String signupUrl;
    public static String defaultPassword;

    public Setup(Properties prop){
        apiUrl = prop.getProperty("tutor.id.api.url");
        usersUrl = apiUrl + "/users";
        signupUrl = apiUrl + "/signup";
        defaultPassword = prop.getProperty("password.default");
    }

}
