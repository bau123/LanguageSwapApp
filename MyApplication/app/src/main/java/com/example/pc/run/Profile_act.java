package com.example.pc.run;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile_act extends AppCompatActivity {

    TextView name;
    TextView interests;
    TextView languagesKnown;
    TextView languagesLearning;
    ImageView profileImage;
    Bitmap bitmap;
    String url = "http://k1.esy.es/pull-profile-image.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_act);

        name = (TextView) findViewById(R.id.nameView);
        interests = (TextView) findViewById(R.id.interestsView);
        languagesKnown = (TextView) findViewById(R.id.languagesKnownView);
        languagesLearning = (TextView) findViewById(R.id.languagesLearningView);
        profileImage = (ImageView) findViewById(R.id.profileImageView);

        name.setText("Name: " + GlobalProfile.profileName);
        interests.setText("Interests:" + GlobalProfile.profileInterests);
        languagesKnown.setText("Languages known:" + GlobalProfile.languagesKnown);
        languagesLearning.setText("Languages learning: " + GlobalProfile.languagesLearning);

        if(GlobalProfile.bitmapString != null) {
            Log.d("PROFILE BITMAP:", GlobalProfile.bitmapString);
            byte[] decodedByte = Base64.decode(GlobalProfile.bitmapString, 0);
            bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

            profileImage.setImageBitmap(bitmap);
        }
    }
}
