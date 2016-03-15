package com.example.pc.run.ActivityTests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.text.method.Touch;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pc.run.Login_act;
import com.example.pc.run.R;
import com.example.pc.run.Register_act;

public class RegisterActivityTest extends ActivityInstrumentationTestCase2<Register_act> {

    public RegisterActivityTest() {
        super(Register_act.class);
    }

    public void testRegister() {
        Register_act activity = getActivity();
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Login_act.class.getName(), null, false);

        final EditText email = (EditText) activity.findViewById(R.id.email);
        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                email.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("testing@kcl.ac.uk");
        getInstrumentation().waitForIdleSync();

        final EditText pass = (EditText) activity.findViewById(R.id.pass);
        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                pass.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("testing123");
        getInstrumentation().waitForIdleSync();

       final EditText pass2 = (EditText) activity.findViewById(R.id.reg_pass2);

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                pass2.requestFocus();
            }
        });


        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("testing123");
        getInstrumentation().waitForIdleSync();

        Button b = (Button) activity.findViewById(R.id.btnRegister);

        TouchUtils.clickView(this, b);

        Register_act nextActivity = (Register_act) activityMonitor.waitForActivity();


        final EditText nameEdit = (EditText) nextActivity.findViewById(R.id.nameEdit);

        // Send string input value
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                nameEdit.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("testing123");
        getInstrumentation().waitForIdleSync();



        assertNotNull("Next act is not launched", nextActivity);
        nextActivity.finish();
    }


}
