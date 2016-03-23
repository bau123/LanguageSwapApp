package com.example.pc.run;

/**
 * Created by Joss on 23/03/2016.
 */
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import junit.framework.Assert;

public class LoginActivityTest extends ActivityInstrumentationTestCase2<Login_act> {
    private Solo solo;

    public LoginActivityTest() {
        super(Login_act.class);

    }

    @Override
    public void setUp() throws Exception {

        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        solo.finishOpenedActivities();
    }

    public void testLogin() throws Exception {
        solo.enterText((EditText) solo.getView(R.id.email_log), "test.test@kcl.ac.uk");
        solo.enterText((EditText) solo.getView(R.id.pass_log), "test1234");
        solo.clickOnView(solo.getView(R.id.btnLogin));

        //Asserts db connection was successful
        Thread.sleep(1000);
        assertEquals(getActivity().result, "success");

        //Assert if toolbar is not null, this will tell if the user succesfully logged in
        Assert.assertNotNull(solo.getView(R.id.container_toolbar));

        //Continue onto app act section
        solo.clickOnImageButton(0);
        solo.clickOnText("Home");
        solo.clickOnView(solo.getView(R.id.spinner));
        solo.clickInList(3);
        solo.clickOnButton(1);
        Assert.assertNotNull(solo.getView(R.id.spinner));

        //Continue onto friends section
        solo.clickOnImageButton(0);
        solo.clickOnText("Friends");
        //Continue onto messages section
        solo.clickOnImageButton(0);
        solo.clickOnText("Messages");
    }

}