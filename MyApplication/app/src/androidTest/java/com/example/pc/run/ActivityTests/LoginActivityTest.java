package com.example.pc.run.ActivityTests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.run.Login_act;
import com.example.pc.run.MainActivity;
import com.example.pc.run.R;

/**
 * Created by Tautvilas on 15/03/2016.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<Login_act> {

    public LoginActivityTest() {
        super(Login_act.class);
    }

    public void testGreet() {
        Login_act activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Login_act.class.getName(), null, false);


        final EditText nameEditText = (EditText) activity.findViewById(R.id.email_log);
        final EditText passEditText = (EditText) activity.findViewById(R.id.pass_log);


        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                nameEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("tautvilas.simkus@kcl.ac.uk");
        getInstrumentation().waitForIdleSync();

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                passEditText.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("magokas1");
        getInstrumentation().waitForIdleSync();

        // Tap "Login" button
        // ----------------------

        Button loginBtn = (Button) activity.findViewById(R.id.btnLogin);

        TouchUtils.clickView(this, loginBtn);

        Login_act nextActivity = (Login_act) activityMonitor.waitForActivity();
        assertNotNull("Next act is not launched", nextActivity);
        nextActivity.finish();
    }


}
