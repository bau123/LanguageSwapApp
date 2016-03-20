package com.example.pc.run;

import android.app.Dialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Adapters.MultiSelectionSpinner;
import com.example.pc.run.Global.GlobalMethds;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private Button saveBtn;
    private MultiSelectionSpinner langKnown, langLearn;
    private EditText interests;
    private String email;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        langKnown = (MultiSelectionSpinner) findViewById(R.id.langKnownSpinner);
        langLearn = (MultiSelectionSpinner) findViewById(R.id.langLearningSpinner);
        interests = (EditText) findViewById(R.id.editInterests);

        langKnown.setItems(GlobalMethds.LanguageArray);
        langLearn.setItems(GlobalMethds.LanguageArray);

        saveBtn = (Button) findViewById(R.id.saveProfileBtn);

        email = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
        getProfileInfo();
    }

    public void getProfileInfo() {
        String url = "http://t-simkus.com/run/pullProfile.php";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);

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

    private void processResult(JSONObject input) throws JSONException {

        JSONArray userInfo = input.getJSONArray("result");
        JSONObject current = userInfo.getJSONObject(0);

        Log.d("NAME:", current.getString("name"));
        Log.d("Interests::", current.getString("interests"));
        Log.d("Languages Known:", current.getString("languagesKnown"));
        Log.d("Languages Learning:", current.getString("languagesLearning"));
        Log.d("BITMAP STRING:", current.getString("photo"));

        interests.setText(current.getString("interests"));

        //Fill in the spinners with current values
        String known = current.getString("languagesKnown");
        ArrayList<String> languagesKnown = new ArrayList<String>(Arrays.asList(known.split(",")));

        String learning = current.getString("languagesLearning");
        ArrayList<String> languagesLearning = new ArrayList<String>(Arrays.asList(learning.split(",")));

        for (int i = 0; i < languagesKnown.size(); i++) {
            for (int j = 0; j < GlobalMethds.LanguageArray.length; i++) {
                if (languagesKnown.get(i).equals(GlobalMethds.LanguageArray[j])) {
                    langKnown.setSelection(j);
                }
            }
        }

        for (int i = 0; i < languagesLearning.size(); i++) {
            for (int j = 0; j < GlobalMethds.LanguageArray.length; i++) {
                if (languagesLearning.get(i).equals(GlobalMethds.LanguageArray[j])) {
                    langLearn.setSelection(j);
                }
            }
        }

        if (current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        } else {
            System.out.println("UNSUCCESSFUL");
        }
    }

    public void saveProfile(View view) {
        String url = "http://t-simkus.com/run/editProfile.php";

        String languagesKnown = langKnown.getSelectedItemsAsString();
        String languagesLearning = langLearn.getSelectedItemsAsString();

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[1]);
        parameters.put("interests", interests.getText().toString());
        parameters.put("known", languagesKnown);
        parameters.put("learning", languagesLearning);

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());

                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    finish();
                    return;

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

    public void changeAuth(View view) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_pass_dialog);
        dialog.setTitle("Change Password");

        final EditText currentPassEdit = (EditText) findViewById(R.id.oldpassEdit);
        final EditText newPassEdit1 = (EditText) findViewById(R.id.newPassEdit1);
        final EditText newPassEdit2 = (EditText) findViewById(R.id.newPassEdit2);



        Button dialogSend = (Button) dialog.findViewById(R.id.change_confirm);
        // If send button is clicked.
        dialogSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPass = currentPassEdit.getText().toString();
                String newPass1 = newPassEdit1.getText().toString();
                String newPass2 = newPassEdit2.getText().toString();


                if(!oldPass.equals(ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[1])){
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Current password is incorrect", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (!newPass1.equals(newPass2) ) {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Does not match. Please re-enter the password.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("email", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0]);
                    parameters.put("password", email);
                    parameters.put("new", email);
                    System.out.println("params made");

                    String pullUrl = "http://t-simkus.com/run/editAuth.php";

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
                    ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
                }

            }
        });

        Button dialogCancel = (Button) dialog.findViewById(R.id.change_cancel);
        // If cancel button is clicked
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
