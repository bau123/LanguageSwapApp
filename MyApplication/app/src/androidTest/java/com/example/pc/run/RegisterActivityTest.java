package com.example.pc.run;

/**
 * Created by Joss on 23/03/2016.
 */
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.example.pc.run.Registration.Register_act;
import com.robotium.solo.Solo;

public class RegisterActivityTest extends ActivityInstrumentationTestCase2<Register_act> {

    private Solo solo;

    public RegisterActivityTest() {
        super(Register_act.class);

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

    public void testRegister() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        solo.enterText((EditText) solo.getView(R.id.email), "random.kampus@kcl.ac.uk");
        solo.enterText((EditText) solo.getView(R.id.pass), "testing111");
        solo.enterText((EditText) solo.getView(R.id.reg_pass2), "testing111");
        solo.clickOnView(solo.getView(R.id.btnRegister));

        //Add another DB CHECKER HERE

        solo.enterText((EditText) solo.getView(R.id.nameEdit), "Testinkivich");
        solo.clickOnView(solo.getView(R.id.langKnownSpinner));
        solo.clickInList(0);
        solo.clickInList(3);
        solo.clickInList(5);
        solo.clickOnButton(1);
        solo.enterText((EditText) solo.getView(R.id.interestsEdit), "Sports");
        solo.clickOnView(solo.getView(R.id.addProfileBtn));
        //TO FIX
    }


}