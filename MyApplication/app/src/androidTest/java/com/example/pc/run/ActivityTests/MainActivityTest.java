package com.example.pc.run.ActivityTests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.example.pc.run.Login_act;
import com.example.pc.run.MainActivity;
import com.example.pc.run.R;
import com.example.pc.run.Register_act;

/**
 * Created by Joss on 07/03/2016.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    Button loginButton;
    Button registerButton;


    public MainActivityTest() {
        super(MainActivity.class);
    }


    public void testActivityExists() {
        MainActivity mainActivity = getActivity();
        assertNotNull(mainActivity);
    }
   // @Test
   // public void testLoginButton(){
  //      MainActivity mainActivity = getActivity();
   //     Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Login_act.class.getName(), null, false);
  //      loginButton = (Button)mainActivity.findViewById(R.id.bt);
//
 //       TouchUtils.clickView(this, loginButton);
//
//        Login_act nextActivity = (Login_act) activityMonitor.waitForActivity();
//        assertNotNull("Next act is not launched", nextActivity);
//        nextActivity.finish();
//    }
//
//    @Test
//    public void testRegisterButton(){
//        MainActivity mainActivity = getActivity();
//        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(Register_act.class.getName(), null, false);
//        registerButton = (Button)mainActivity.findViewById(R.id.registerButton);
//
//        TouchUtils.clickView(this, registerButton);
//
//        Register_act nextActivity = (Register_act) activityMonitor.waitForActivity();
//        assertNotNull("Next act is not launched", nextActivity);
//        nextActivity.finish();
//    }

}

