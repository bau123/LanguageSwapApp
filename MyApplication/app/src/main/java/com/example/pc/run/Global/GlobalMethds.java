package com.example.pc.run.Global;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
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


    public static Bitmap stringToBitmap(String image) {
        Bitmap bitmap = null;
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
        }
        return bitmap;
    }

    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
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
