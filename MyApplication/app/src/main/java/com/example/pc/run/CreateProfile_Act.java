package com.example.pc.run;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.MultiSelectionSpinner;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateProfile_Act extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    EditText name;
    EditText interests;
    String languagesKnown;
    String languagesLearning;
    MultiSelectionSpinner langKnownSpinner;
    MultiSelectionSpinner langLearningSpinner;
    private String email;
    private String pass;
    String url = "http://t-simkus.com/run/insert-profile-db.php";
    Profile profile;
    ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_);

        profileImage = (ImageView)findViewById(R.id.profileImage);
        langKnownSpinner = (MultiSelectionSpinner)findViewById(R.id.langKnownSpinner);
        langLearningSpinner = (MultiSelectionSpinner)findViewById(R.id.langLearningSpinner);
        langKnownSpinner.setListener(this);langLearningSpinner.setListener(this);

        String[] array = {"Akan", "Assamese", "Azerbaijani", "Belarusian", "Bengali",
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

        langKnownSpinner.setItems(array);langLearningSpinner.setItems(array);

        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");

        if(GlobalBitmap.bitmap != null){
            profileImage.setImageBitmap(GlobalBitmap.bitmap);
        }
        name = (EditText)findViewById(R.id.nameEdit);
        interests = (EditText)findViewById(R.id.interestsEdit);
    }

    public void addProfileInfo(View view) {
        System.out.println("making params");
        Map<String, String> parameters = new HashMap<>();

        /*
            TODO: set languagesKnown/languagesLearning correctly
         */

        languagesKnown = langKnownSpinner.getSelectedItemsAsString();
        languagesLearning = langLearningSpinner.getSelectedItemsAsString();
        System.out.println("CHECKING FUNCTIONALITY LANGUAGES " + langKnownSpinner.getSelectedItemsAsString() + " \n" + langLearningSpinner.getSelectedItemsAsString());

        parameters.put("password", pass);
        parameters.put("email", email);
        parameters.put("name", name.getText().toString());
        parameters.put("languagesKnown", languagesKnown);
        parameters.put("languagesLearning", languagesLearning);
        parameters.put("interests", interests.getText().toString());

        if(GlobalBitmap.bitmap != null){
            String photo = getStringImage(GlobalBitmap.bitmap);
            parameters.put("photo", photo);
        }

        System.out.println("params made");
        Log.d("Email Passed:", email);

        profile = new Profile(name.getText().toString(), languagesKnown,
                languagesLearning, interests.getText().toString());

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    //Determines whether input is valid.
    private void processResult(JSONObject input) throws InterruptedException {
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {
            ApplicationSingleton.getInstance().getPrefManager().storeProfile(profile); //STORE PROFILE WITH THIS
            Intent intent = new Intent(this, Login_act.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Adding Profile info failed", Toast.LENGTH_LONG).show();
        }

    }

    public void uploadImage(View v){
        Intent intent = new Intent(this, UploadImage_act.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

    }
}
