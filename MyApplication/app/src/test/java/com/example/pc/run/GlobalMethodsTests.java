package com.example.pc.run;

/**
 * Created by Joss on 19/03/2016.
 */
import com.example.pc.run.Global.GlobalMethds;

import org.junit.Test;

import static org.junit.Assert.*;

public class GlobalMethodsTests {
    GlobalMethds methds = new GlobalMethds();

    @Test
    public void verifyEmail(){
        assertTrue(methds.validateEmail("test.test@kcl.ac.uk"));
        assertFalse(methds.validateEmail("123@kcl"));
        assertFalse(methds.validateEmail("test@kcl.ac.uk"));
        assertTrue(methds.validateEmail("random.yes@kcl.ac.uk"));
    }
}
