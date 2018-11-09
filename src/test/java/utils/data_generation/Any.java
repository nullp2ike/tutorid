package utils.data_generation;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Any {

    public static String alphabetic(int min, int max){
        int i = max - min;
        return RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(i, max + 1));
    }

    public static String alphaNumeric(int min, int max){
        int i = max - min;
        return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(i, max + 1));
    }

    public static String studentUsername(){
        return "ST" + alphaNumeric(2, 18);
    }

    public static String tutorUsername(){
        return "TU" + alphaNumeric(2, 18);
    }

    public static String email(String username) {
        String email = "tutoridtest+" + username + "@gmail.com"; //Password for this email account is Valekala3
        return email;
    }

}
