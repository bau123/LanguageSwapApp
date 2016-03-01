package com.example.pc.run.LocationServices;

import android.content.Context;
import android.location.Location;

import com.example.pc.run.Network_Utils.ParseJSON;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class CoordinatesToString  {

    Context context;
    public String[] address;
    public String campus = "Not at any campus";
    public double latitude = 0,longitude = 0;
    public GPSTracker gps;

    public CoordinatesToString(Context context) {
        this.context = context;
        convert();
        setCampus();
    }




    private void setCampus() {
        for(String s : address) {
            s = s.toString();
            System.out.println("Address " +s);
            if(s.toLowerCase().contains("strand")) {
                campus = "Strand";
            }

            else if(s.toLowerCase().contains("se1 9")) {
                campus = "Franklin-Wilkins building";
            }
            else if(s.toLowerCase().contains("se1 8")) {
                campus = "James Clerk Maxwell building";
            }

            else if(s.toLowerCase().contains("wc2a")) {
                campus = "Maughan Library & Information Services Centre";
            }
            else if(s.toLowerCase().contains("wc2b 5")) {
                campus = "Durry lane building";
            }
            else if(s.toLowerCase().contains("wc2b 6")) {
                campus = "Virginia woolf building";
            }

        }

    }

    /*
    @Return: formatted address
     */
    public void convert() {
        gps = new GPSTracker(context);

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
        System.out.println(lookupLink + " this is the lookup link");

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
