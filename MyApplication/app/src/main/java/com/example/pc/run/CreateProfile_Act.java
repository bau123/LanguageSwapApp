package com.example.pc.run;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class CreateProfile_Act extends AppCompatActivity {

    EditText name;
    EditText interests;
   // EditText languagesKnown;
   // EditText languagesLearning;
    String email;
    String url = "http://t-simkus.com/run/insert-profile-db.php";
    Profile profile;
    ImageView profileImage;

    private Spinner spin1;
    private Spinner spin2;
    private Spinner spin3;
    private Spinner spin4;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile_);

        profileImage = (ImageView)findViewById(R.id.profileImage);

        email = getIntent().getStringExtra("email");

        if(GlobalBitmap.bitmap != null){
            profileImage.setImageBitmap(GlobalBitmap.bitmap);
        }
        name = (EditText)findViewById(R.id.nameEdit);
        interests = (EditText)findViewById(R.id.interestsEdit);
       // languagesKnown = (EditText)findViewById(R.id.langKnownEdit);
       // languagesLearning = (EditText)findViewById(R.id.langLearningEdit);

        //limit the size of the editText
        EditText myEditText = (EditText) findViewById(R.id.nameEdit);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(20); //Filter to 10 characters
        myEditText.setFilters(filters);

        spin1 = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin3 = (Spinner) findViewById(R.id.spinner3);
        spin4 = (Spinner) findViewById(R.id.spinner4);

        adapter = ArrayAdapter.createFromResource(this, R.array.countriesArr, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin1.setAdapter(adapter);
        spin2.setAdapter(adapter);
        spin3.setAdapter(adapter);
        spin4.setAdapter(adapter);

        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText((getBaseContext()), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText((getBaseContext()), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText((getBaseContext()), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText((getBaseContext()), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void addProfileInfo(View view) {
        System.out.println("making params");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("name", name.getText().toString());
        //parameters.put("languagesKnown", languagesKnown.getText().toString());
       // parameters.put("languagesLearning", languagesLearning.getText().toString());
        parameters.put("interests", interests.getText().toString());

        if(GlobalBitmap.bitmap != null){
            String photo = getStringImage(GlobalBitmap.bitmap);
            parameters.put("photo", photo);
        }

        System.out.println("params made");
        Log.d("Email Passed:", email);

       //profile = new Profile(name.getText().toString(), languagesKnown.getText().toString(),
             //  languagesLearning.getText().toString(), interests.getText().toString());

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
            Intent intent = new Intent(this, App_act.class);
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
}
