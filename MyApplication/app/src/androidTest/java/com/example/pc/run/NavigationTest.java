package com.example.pc.run;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.Random;

//New tests
public class NavigationTest extends ActivityInstrumentationTestCase2<Login_act> {
    private Solo solo;
    /*
        TODO: Please delete instance of application on phone before executing test!
     */

    public NavigationTest() {
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
        solo.unlockScreen();
        //Login
        solo.clearEditText((EditText) solo.getView(R.id.email_log));
        solo.enterText((EditText) solo.getView(R.id.email_log), "tautvilas.simkus@kcl.ac.uk");
        solo.enterText((EditText) solo.getView(R.id.pass_log), "ma1");
        solo.clickOnView(solo.getView(R.id.btnLogin));
        solo.sleep(2000);



        //Use spinner to search by campus
        solo.clickOnView(solo.getView(R.id.spinner));
        solo.clickInList(3);
        solo.clickOnButton(1);
        solo.sleep(1000);
        //

        //Send a friend request
        solo.clickOnView(solo.getView(R.id.addFavBtn));
        solo.sleep(1000);

        //Go to friends
        solo.clickOnImageButton(0);
        solo.clickOnText("Friends");
        solo.sleep(1000);


        //Write a review
        solo.clickOnView(solo.getView(R.id.reviewButton));
        solo.clickOnView(solo.getView(R.id.typeSpinner));
        solo.clickInList(1);
        solo.enterText((EditText) solo.getView(R.id.commentEdit), "This student is a very good listener");
        solo.goBack();
        solo.sleep(1000);

        //View Profile
        solo.clickOnView(solo.getView(R.id.frProfileImage));
        solo.sleep(1000);
        solo.clickOnView(solo.getView(R.id.reviewButton));
        solo.sleep(1000);
        solo.goBack();
        solo.goBack();

        //Chat with him
        solo.clickOnView(solo.getView(R.id.frMessageButton));
        solo.enterText((EditText) solo.getView(R.id.message), "Hello there my man");
        solo.clickOnView(solo.getView(R.id.btn_send));
        solo.sleep(1000);
        solo.goBack();

        //Chat through messages
        solo.clickOnImageButton(0);
        solo.clickOnText("Messages");
        solo.sleep(200);
        solo.clickOnText("pm");
        solo.sleep(1000);
        solo.goBack();


        //Check my profile
        solo.clickOnImageButton(0);
        solo.clickOnText("Profile");
        solo.clickOnView(solo.getView(R.id.reviewButton));
        solo.sleep(1000);
        solo.clickOnText("Learning Reviews");
        solo.sleep(1000);
        solo.goBack();
        solo.clickOnView(solo.getView(R.id.editProfile));

        solo.clickOnView(solo.getView(R.id.langKnownSpinner));
        Random r = new Random();
        solo.clickInList(r.nextInt(15));
        solo.clickOnButton(1);

        solo.clickOnView(solo.getView(R.id.langLearningSpinner));
        solo.clickInList(r.nextInt(15));
        solo.clickOnButton(1);
        solo.clearEditText((EditText) solo.getView(R.id.editInterests));
        solo.enterText((EditText) solo.getView(R.id.editInterests),"Running");
        solo.clickOnView(solo.getView(R.id.saveProfileBtn));
        solo.sleep(1000);


        //Check About us
        solo.clickOnMenuItem("About Us");
        solo.goBack();

        //Check Code of Conduct
        solo.clickOnMenuItem("Code of conduct");
        solo.goBack();

        //Sign out
        solo.clickOnMenuItem("Sign Out");
    }

}