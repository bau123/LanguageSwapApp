package com.example.pc.run;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalProfile;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login_act extends AppCompatActivity {

    private EditText email, pass;
    private TextInputLayout inputEmail, inputPassword;
    String url = "http://t-simkus.com/run/checkPass.php";
    String mEmail;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);

        inputEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        email = (EditText) findViewById(R.id.email_log);
        pass = (EditText) findViewById(R.id.pass_log);

        email.addTextChangedListener(new MyTextWatcher(inputEmail));
       //  pass.addTextChangedListener(new MyTextWatcher(inputEmail)); ADD LATER!!!!!!!!
    }

    //Checks if email is in correct form
    public boolean validateEmail() {
        String re1 = "((?:[a-z][a-z]+))";    // Word 1
        String re2 = "(.)";    // Any Single Character 1
        String re3 = "((?:[a-z][a-z]+))";    // Word 2
        String re4 = "(@)";    // Any Single Character 2
        String re5 = "(kcl\\.ac\\.uk)";    // Fully Qualified Domain Name 1

        Pattern p = Pattern.compile(re1 + re2 + re3 + re4 + re5, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = p.matcher(email.getText().toString());

        Boolean result = matcher.matches();
        if(result){
            inputEmail.setErrorEnabled(false);
        }else{
            inputEmail.setError(getString(R.string.log_email_error));
            requestFocus(inputEmail);
        }
        return result;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void login(View view) {
        if(validateEmail()){
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("email", email.getText().toString());
            parameters.put("password", pass.getText().toString());

            mEmail = email.getText().toString();
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
    }

    public void adminLogin(View view) {
        System.out.println("Making params");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", "tautvilas.simkus@kcl.ac.uk");
        parameters.put("password", "magokas1");
        System.out.println("params made");

        mEmail = email.getText().toString();
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

    private void processResult(JSONObject input) throws InterruptedException {
        String result = "";
        try {
            result = input.getString("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("success")) {
            ApplicationSingleton.getInstance().getPrefManager().storeAuthentication(email.getText().toString(), pass.getText().toString());
            //Pulls the profile info of the user logging in
            pullProfile();
            GlobalProfile.profileEmail = mEmail;
            Thread.sleep(100);
            Intent intent = new Intent(this, App_act.class);
            startActivity(intent);
        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Sorry the password is incorrect", Toast.LENGTH_LONG).show();
        }
        //If user with the email exists
        else if (result.equals("failure - exists")) {
            Toast.makeText(getApplicationContext(), "Sorry the password is incorrect", Toast.LENGTH_LONG).show();
            //login tries counter
            counter++;
            //
            if (counter > 5) {
                Toast.makeText(getApplicationContext(), "Too many tries! \n Sorry this account has now been locked for 15 minutes", Toast.LENGTH_LONG).show();
                lockAccount(email.getText().toString());
            }
        }
        //When account is locked out from too many tries
        else if (result.equals("locked")) {
            Toast.makeText(getApplicationContext(), "The account is locked", Toast.LENGTH_LONG).show();
        }
    }

    public void lockAccount(String email) {
        String lockUrl = "http://t-simkus.com/run/lockAccount.php";

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email);

        Requests jsObjRequest = new Requests(Request.Method.POST, lockUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    //Pulls the user profile info and stores in shared pref
    public void pullProfile() {
        String pullUrl = "http://t-simkus.com/run/pullProfile.php";

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email.getText().toString());

        Requests jsObjRequest = new Requests(Request.Method.POST, pullUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());

                    JSONArray result = response.getJSONArray("result");
                    JSONObject current = result.getJSONObject(0);
                    Profile p = new Profile();
                    //Sets all the pull info into the temporally profile object
                    p.setEmail(email.getText().toString());
                    p.setProfilePicture(current.getString("photo"));
                    p.updateName(current.getString("name"));
                    p.updateInterests(current.getString("interests"));
                    p.updateLanguagesKnown(current.getString("languagesKnown"));
                    p.updateLanguagesLearning(current.getString("languagesLearning"));

                    //Stores Profile into the shared pref
                    ApplicationSingleton.getInstance().getPrefManager().storeProfile(p);

                } catch (JSONException e) {
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email_log:
                    validateEmail();
                    break;
               // case R.id.pass_log:
                 //   validatePassword();        PUT BACK IN AT END!!!!!!!!!!!!!!!!
                   // break;
            }
        }
    }

}
