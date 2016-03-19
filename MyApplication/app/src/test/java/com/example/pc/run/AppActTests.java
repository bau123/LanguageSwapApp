package com.example.pc.run;

import android.view.View;

import com.example.pc.run.Adapters.MultiSelectionSpinner;
import com.example.pc.run.LocationServices.SelectedCampus;
import com.example.pc.run.LocationServices.UserLocation;

import org.junit.Test;

/**
 * Created by Joss on 19/03/2016.
 */

import java.util.ArrayList;
import static org.junit.Assert.*;

public class AppActTests extends App_act {

    public AppActTests(){
    }
    @Test
    public void testTranslateCampus(){
        ArrayList<SelectedCampus> campus = new ArrayList<>();
        SelectedCampus a = new SelectedCampus(0, true);
        SelectedCampus b = new SelectedCampus(2, true);
        SelectedCampus c = new SelectedCampus(4, true);
        campus.add(a);campus.add(b);campus.add(c);
        translateCampus(campus);

        assertEquals("Strand", campuses.get(0));
        assertEquals("James Clerk Maxwell", campuses.get(1));
        assertEquals("Durry Lane", campuses.get(2));
    }

    @Test
    public void testGetCampusPeople(){
        UserLocation a = new UserLocation("test.test@kcl.ac.uk", "Strand");
        UserLocation b = new UserLocation("test2.test2@kcl.ac.uk", "James Clerk Maxwell");
        UserLocation c = new UserLocation("test3.test3@kcl.ac.uk", "Durry Lane");
        UserLocation d = new UserLocation("test4.test4@kcl.ac.uk", "Maughan Library");
        arrayUsers.add(a); arrayUsers.add(b); arrayUsers.add(c);arrayUsers.add(d);
        campuses.add("Strand");campuses.add("James Clerk Maxwell");campuses.add("Durry Lane");
        getCampusPeople();

        assertTrue(selectedEmails.contains("test.test@kcl.ac.uk"));
        assertTrue(selectedEmails.contains("test2.test2@kcl.ac.uk"));
        assertTrue(selectedEmails.contains("test3.test3@kcl.ac.uk"));
        assertFalse(selectedEmails.contains("test4.test4@kcl.ac.uk"));
    }
}
