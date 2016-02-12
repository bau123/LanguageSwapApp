package com.example.pc.run.Network_Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessJsons {

    //For json's which have only one object
    public static String processSingleJson(String input){
        String output = "";
        try{
            JSONObject ob1 = new JSONObject(input.toString());
            output =  ob1.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return output;
    }

}
