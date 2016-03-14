package com.example.pc.run;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalMethds;
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
    private CoordinatorLayout coordinatorLayout;
    String url = "http://t-simkus.com/run/checkPass.php";

    String mEmail;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_act);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        inputEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        email = (EditText) findViewById(R.id.email_log);
        pass = (EditText) findViewById(R.id.pass_log);

        email.addTextChangedListener(new MyTextWatcher(inputEmail));

        //If User has already logged in before it automatically logs in for them.
        if(ApplicationSingleton.getInstance().getPrefManager().checkAccount()){
            login(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0] ,ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[1]);
        }else{
            ApplicationSingleton.getInstance().getPrefManager().clear();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void login(View view) {
        login(email.getText().toString(), pass.getText().toString());
    }

    protected void login(String email ,String pass){
        //Checks if there is an internet connection     //FIXXXXXXXXXXXXXXXXXX
        /*if (!GlobalMethds.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }*/
        //Checks if the email is in the correct format
        if (GlobalMethds.validateEmail(email)) {
            inputEmail.setErrorEnabled(false);

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("email", email);
            parameters.put("password", pass);

            mEmail = email;
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
        } else {
            inputEmail.setError(getString(R.string.log_email_error));
            requestFocus(inputEmail);
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
            //Stores the authentication details of the user
            ApplicationSingleton.getInstance().getPrefManager().storeAuthentication(email.getText().toString(), pass.getText().toString());

            //Pulls the profile info of the user logging in
            PullProfile pull = new PullProfile();
            pull.pullInformation();

            //Starts the main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        } else if (result.equals("failure")) {
            Toast.makeText(getApplicationContext(), "Sorry the password is incorrect", Toast.LENGTH_LONG).show();
        }
        //If user with the email exists
        else if (result.equals("failure - exists")) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Incorrect Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            //login tries counter
            counter++;
            //
            if (counter > 5) {
                Snackbar tries = Snackbar.make(coordinatorLayout, "Too many tries! \n" + " Sorry this account has now been locked for 15 minutes", Snackbar.LENGTH_LONG);
                tries.show();
                lockAccount(email.getText().toString());
            }
        }
        //When account is locked out from too many tries
        else if (result.equals("locked")) {
            Snackbar tries = Snackbar.make(coordinatorLayout, "Account is locked", Snackbar.LENGTH_LONG);
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

    public void toRegister(View view) {
        Intent intent = new Intent(this, Register_act.class);
        startActivity(intent);
    }

    public void forgottenPass(View view) {
        Intent intent = new Intent();
        startActivity(intent);
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
                    GlobalMethds.validateEmail(email.getText().toString());
                    break;
            }
        }
    }

}
