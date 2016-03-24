package com.example.pc.run.Profile;

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
import com.example.pc.run.R;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private MultiSelectionSpinner langKnown, langLearn;
    private EditText interests;
    private String email;
    private CoordinatorLayout coordinatorLayout;
    private List<String> langKnownList,langLearnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*
            Initialising key variables
         */
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);


        langKnown = (MultiSelectionSpinner) findViewById(R.id.langKnownSpinner);

        //Init spinner
        buildSpinnerKnown(langKnown);
        langLearn = (MultiSelectionSpinner) findViewById(R.id.langLearningSpinner);
        //Init spinner2
        buildSpinnerLearn(langLearn);


        interests = (EditText) findViewById(R.id.editInterests);

        //Set Language Array as variables for language list
        langKnown.setItems(GlobalMethds.LanguageArray);
        langLearn.setItems(GlobalMethds.LanguageArray);


        //Get email from shared preference and calls db to pull info
        email = ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[0];
        getProfileInfo();
    }

    /*
        Setting listeners for both spinner
     */
    public void buildSpinnerKnown(MultiSelectionSpinner spinner) {
        spinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                //
            }

            @Override
            public void selectedStrings(List<String> strings) {
                langKnownList = strings;
            }
        });
    }

    public void buildSpinnerLearn(MultiSelectionSpinner spinner) {
        spinner.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                //
            }

            @Override
            public void selectedStrings(List<String> strings) {
                langLearnList = strings;
            }
        });
    }



    /*
        Calls database to pull user profile with value email as primary key
     */
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

    /*
        Processes JSON result from database query
     */
    private void processResult(JSONObject input) throws JSONException {

        ///Get's array of profile from db and takes value at 0
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

        /*
            Sets language selections form global langarray
         */
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

        //Returns successful if database query worked
        if (current.getString("name") != null) {
            System.out.println("SUCCESSFUL");
        } else {
            System.out.println("UNSUCCESSFUL");
        }
    }

    /*
         Database call to update user information
     */
    public void saveProfile(View view) {
        String url = "http://t-simkus.com/run/editProfile.php";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", ApplicationSingleton.getInstance().getPrefManager().getAuthentication()[1]);
        parameters.put("interests", interests.getText().toString());
        parameters.put("languagesKnown", langKnownList.toString());
        parameters.put("languagesLearning", langLearnList.toString());

        Requests jsObjRequest = new Requests(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    try {
                        /*
                            Actions depending on result
                         */
                        String result = response.getString("message");
                        if (result.equals("success")) {
                            Log.d("Response: ", response.toString());
                            Toast.makeText(getApplicationContext(), "Your profile has been updated", Toast.LENGTH_LONG).show();
                            finish();

                        } else {
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

    public void changeAuth(View view) {

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.change_pass_dialog);
        dialog.setTitle("Change Password");

        final EditText currentPassEdit = (EditText) dialog.findViewById(R.id.oldpassEdit);
        final EditText newPassEdit1 = (EditText) dialog.findViewById(R.id.newPassEdit1);
        final EditText newPassEdit2 = (EditText) dialog.findViewById(R.id.newPassEdit2);



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
                    parameters.put("password", oldPass);
                    parameters.put("newPass", newPass2);
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
                                        Log.d("Response: ", response.toString());
                                        Toast.makeText(getApplicationContext(), "New password has been set.", Toast.LENGTH_LONG).show();
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
