package com.example.pc.run.LocationServices;

import android.content.Context;

import com.example.pc.run.Network_Utils.ParseJSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Tautvilas on 14/02/2016.
 */
public class CoordinatesToString  {

    Context context;
    public String[] address;
    public String campus = "Not in any campus";
    public double latitude = 0,longitude = 0;

    public CoordinatesToString(Context context) {
        this.context = context;
        convert();
        setCampus();
    }

    private void setCampus() {
        for(String s : address) {
            if(s.toLowerCase().contains("strand")) {
                campus = "Strand";
            }
            else {
                campus = "Not at any campus";
            }
        }
    }

    /*
    @Return: formatted address
     */
    public void convert() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            this.latitude = gps.getLatitude();
            this.longitude = gps.getLongitude();

        }

        else {
            gps.showSettingsAlert();
        }

        /*
        Get JSON object
        */
        final String lookupLink = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&key=AIzaSyD_bf5Bw26seqpx7IQRt3pr9zQd6j-tXLs";
        System.out.println(lookupLink);

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future f = es.submit(new ParseJSON(lookupLink));

        try {
            address = (String[]) f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
