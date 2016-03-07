package com.example.pc.run;

import android.test.ActivityInstrumentationTestCase2;
import android.test.AndroidTestCase;

import org.junit.Test;


/**
 * Created by Joss on 07/03/2016.
 */
public class JUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public JUnitTest() {
        super(MainActivity.class);
    }

    @Test
    public void testActivityExists() {
        MainActivity mainActivity = getActivity();
        assertNotNull(mainActivity);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}
