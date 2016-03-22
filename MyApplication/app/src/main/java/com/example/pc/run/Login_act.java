package com.example.pc.run;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalMethds;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.Objects.Profile;
import com.example.pc.run.SharedPref.ApplicationSingleton;
import com.example.pc.run.Video.BaseActivity;
import com.example.pc.run.Video.SinchService;
import com.sinch.android.rtc.SinchError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_act extends BaseActivity implements SinchService.StartFailedListener {

    private EditText email, pass;
    private TextInputLayout inputEmail, inputPassword;
    private CoordinatorLayout coordinatorLayout;
    String url = "http://t-simkus.com/run/checkPass.php";
    String mEmail;
    int counter = 0;
    private String emailSt, passSt;
    public String result;

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
        if (ApplicationSingleton.getInstance().getPrefManager().checkAccount()) {
            System.out.println("Account already in device");
            login(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0], ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[1]);
        } else {
            System.out.println("No Account in device");
            ApplicationSingleton.getInstance().getPrefManager().clear();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void login(View view) {
        login(email.getText().toString().trim(), pass.getText().toString());
    }

    protected void login(String email, String pass) {
        System.out.println("Logging in ");
        if (!checkNetwork()) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }

        //Checks if the email is in the correct format
        if (GlobalMethds.validateEmail(email)) {
            System.out.println("Email is valid");
//            inputEmail.setErrorEnabled(false);

            emailSt = email;
            passSt = pass;

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
            jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input) throws InterruptedException {
        System.out.println("In processResult");
        result = "";
        try {
            result = input.getString("message");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.equals("success")) {
            //Stores the authentication details of the user
            ApplicationSingleton.getInstance().getPrefManager().storeAuthentication(emailSt, passSt);

            //Pulls the profile info of the user logging in
            pullInformation(emailSt);

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
                lockAccount(emailSt);
            }
        }
        //When account is locked out from too many tries
        else if (result.equals("locked")) {
            Snackbar tries = Snackbar.make(coordinatorLayout, "Account is locked", Snackbar.LENGTH_LONG);
        }
    }

    public void pullInformation(String email) {
        System.out.println("Making params");
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", email);
        System.out.println("params made");

        String pullUrl = "http://t-simkus.com/run/pullProfile.php";

        Requests jsObjRequest = new Requests(Request.Method.POST, pullUrl, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    processProfileInfo(response);
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
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processProfileInfo(JSONObject input) throws JSONException {

        JSONArray userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        Profile profile = new Profile(current.getString("name"), current.getString("languagesKnown"),
                current.getString("languagesLearning"), current.getString("interests"));

        ApplicationSingleton.getInstance().getPrefManager().storeProfile(profile);
        ApplicationSingleton.getInstance().getPrefManager().storeProfileImage("photo");

        if (current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
            //Starts the main activity
            if (!getSinchServiceInterface().isStarted()) {
                getSinchServiceInterface().startClient(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
            }else{
                openMainAct();
            }


        } else {
            System.out.println("UNSUCCESSFUL");
        }
    }

    @Override
    public void onStarted() {
        openMainAct();
    }

    @Override
    protected void onServiceConnected() {
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    public void openMainAct(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.forgot_dialog);
        dialog.setTitle("Forgotten Password");

        // set the custom dialog components - text, image and button
        final EditText forgotEmail = (EditText) findViewById(R.id.forgotEmail);

        Button dialogSend = (Button) dialog.findViewById(R.id.forgotSendBtn);
        // If send button is clicked.
        dialogSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotEmail.getText().toString().trim();

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", email);
                System.out.println("params made");

                String pullUrl = "http://t-simkus.com/run/resetPassword.php";

                Requests jsObjRequest = new Requests(Request.Method.POST, pullUrl, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            System.out.println(response.toString());
                            try {
                                String result = response.getString("message");
                                if (result.equals("success")) {
                                    Toast.makeText(getApplicationContext(), "New password has been sent. Please check your email", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Sorry given account is not found ", Toast.LENGTH_LONG).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.forgotCancelBtn);
        // If cancel button is clicked
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean checkNetwork() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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
