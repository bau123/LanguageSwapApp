package com.example.pc.run;

import android.content.Intent;
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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.pc.run.Global.GlobalMethds;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Register_act extends AppCompatActivity {

    private EditText pass, email, pass2;
    private String emailString = null;
    private String passwordString = null;
    private TextInputLayout inputLayoutEmail, inputLayoutPass;
    private CoordinatorLayout coordinatorLayout;
    String url = "http://t-simkus.com/run/checkEmail.php";
    int i;
    ProgressBar passBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_act);

        i = 1;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordReg);

        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPass = (TextInputLayout) findViewById(R.id.input_layout_password);

        pass = (EditText) findViewById(R.id.pass);
        pass2 = (EditText) findViewById(R.id.reg_pass2);
        email = (EditText) findViewById(R.id.email);

        passBar = (ProgressBar) findViewById(R.id.progressBar);

        email.addTextChangedListener(new MyTextWatcher(inputLayoutEmail));
        pass2.addTextChangedListener(new MyTextWatcher(inputLayoutPass));

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pass.getText().toString().length() == 0) {
                    pass.setError("Enter your password..!");
                } else {
                    caculation();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void register(View view) {
        emailString = email.getText().toString();
        passwordString = pass.getText().toString();
        String passwordString2 = pass2.getText().toString();

        //Checks if both password are the same
        if (!passwordString.equals(passwordString2)) {
            inputLayoutPass.setError(getString(R.string.reg_pass_error));
            requestFocus(inputLayoutEmail);
            return;
        }

        if (passwordString.length() < 8) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Password must be at least 8 characters long", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }

        //Checks if the email is in the correct format
        if (!GlobalMethds.validateEmail(emailString)) {
            inputLayoutEmail.setError(getString(R.string.log_email_error));
            requestFocus(inputLayoutEmail);
            return;
        }

        checkEmailAvailable();

    }

    public void checkEmailAvailable(){

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("email", emailString);

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.print(response);
                    String output = response.getString("message");
                    processResult(output);
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

    public void processResult(String output){
        if (output.equals("found")) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Sorry, email is already taken", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else if (output.equals("notFound")) {
            Intent intent = new Intent(this, CreateProfile_Act.class);
            intent.putExtra("email", emailString);
            intent.putExtra("pass", passwordString);
            startActivity(intent);
        }
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
                case R.id.reg_pass2:

                    break;
            }
        }

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //Calculates the password strength
    protected void caculation() {
        String temp = pass.getText().toString();
        i = i + 1;

        int length = 0, uppercase = 0, lowercase = 0, digits = 0, symbols = 0, bonus = 0, requirements = 0;

        int lettersonly = 0, numbersonly = 0, cuc = 0, clc = 0;

        length = temp.length();
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isUpperCase(temp.charAt(i)))
                uppercase++;
            else if (Character.isLowerCase(temp.charAt(i)))
                lowercase++;
            else if (Character.isDigit(temp.charAt(i)))
                digits++;

            symbols = length - uppercase - lowercase - digits;
        }

        for (int j = 1; j < temp.length() - 1; j++) {
            if (Character.isDigit(temp.charAt(j)))
                bonus++;
        }

        for (int k = 0; k < temp.length(); k++) {
            if (Character.isUpperCase(temp.charAt(k))) {
                k++;
                if (k < temp.length()) {
                    if (Character.isUpperCase(temp.charAt(k))) {
                        cuc++;
                        k--;
                    }
                }
            }
        }

        for (int l = 0; l < temp.length(); l++) {
            if (Character.isLowerCase(temp.charAt(l))) {
                l++;
                if (l < temp.length()) {
                    if (Character.isLowerCase(temp.charAt(l))) {

                        clc++;
                        l--;
                    }
                }
            }
        }

        if (length > 7) {
            requirements++;
        }
        if (uppercase > 0) {
            requirements++;
        }
        if (lowercase > 0) {
            requirements++;
        }
        if (digits > 0) {
            requirements++;
        }
        if (symbols > 0) {
            requirements++;
        }
        if (bonus > 0) {
            requirements++;
        }
        if (digits == 0 && symbols == 0) {
            lettersonly = 1;
        }
        if (lowercase == 0 && uppercase == 0 && symbols == 0) {
            numbersonly = 1;
        }

        int Total = (length * 4) + ((length - uppercase) * 2)
                + ((length - lowercase) * 2) + (digits * 4) + (symbols * 6)
                + (bonus * 2) + (requirements * 2) - (lettersonly * length * 2)
                - (numbersonly * length * 3) - (cuc * 2) - (clc * 2);

        if (Total < 30) {
            passBar.setProgress(Total - 15);
        } else if (Total >= 40 && Total < 50) {
            passBar.setProgress(Total - 20);
        } else if (Total >= 56 && Total < 70) {
            passBar.setProgress(Total - 25);
        } else if (Total >= 76) {
            passBar.setProgress(Total - 30);
        } else {
            passBar.setProgress(Total - 20);
        }

    }

}
