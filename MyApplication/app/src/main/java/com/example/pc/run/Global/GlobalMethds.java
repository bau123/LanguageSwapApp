package com.example.pc.run.Global;


import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalMethds {


    public static String[] LanguageArray = {"Akan", "Assamese", "Azerbaijani", "Belarusian", "Bengali",
            "Berbe", "Bhojpuri", "Bulgarian", "Bengali", "Burmese",
            "Cebuano", "Chattisgarh", "Chitagonian", "Czech", "Dekhni",
            "Dsindhi", "Dutch", "Egyptian Arabic", "English", "French",
            "Fula", "Fulfulde", "Gan Chinese", "German", "Greek",
            "Gugarato", "Haitan Creole", "Hakka Chinese", "Haryanvi", "Hausa",
            "Hebrew", "Hiligaynon", "Hindi", "Hungarian", "Igbo",
            "Ilokano", "Italian", "Japanese", "Jin Yu Chinese", "Kannada",
            "Kazah", "Khme", "Kinyarwanda", "Korean", "Kurdish",
            "Levantine Arabic", "Madurese", "Magadhi", "Maghrebi Arabic", "Malagasy",
            "Marwari", "Arabic", "Panjabi", "Persian", "Polish",
            "Portuguese", "Romanian", "Russian", "Saraiki", "Swedish",
            "Shinhala", "Somali", "Spanish", "Sudanese", "Tamil",
            "Thai", "Turkish", "Ukrainian", "Urdu", "Uzbek",
            "Vietnamese", "Wu Chinese", "Xiang Chinese", "Zulu", "Yoruba",};


    //Used to check if there is an internet connection
    public static boolean isNetworkAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name

            if (ipAddr.equals("")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            return false;
        }
    }

    //Checks if email is in correct form
    public static boolean validateEmail(String email) {
        String re1 = "((?:[a-z][a-z]+))";    // Word 1
        String re2 = "(.)";    // Any Single Character 1
        String re3 = "((?:[a-z][a-z]+))";    // Word 2
        String re4 = "(@)";    // Any Single Character 2
        String re5 = "(kcl\\.ac\\.uk)";    // Fully Qualified Domain Name 1

        Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = p.matcher(email);

        return matcher.matches();
    }


}
