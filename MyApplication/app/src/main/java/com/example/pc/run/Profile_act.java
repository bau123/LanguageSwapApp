package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.pc.run.Objects.Profile;

public class Profile_act extends AppCompatActivity {

    TextView name, languagesKnown, languagesLearning, interests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_act);
        Profile profile = new Profile();

        name = (TextView)findViewById(R.id.nameField);
        languagesKnown = (TextView)findViewById(R.id.langKnownField);
        languagesLearning = (TextView)findViewById(R.id.langLearningField);
        interests = (TextView)findViewById(R.id.interestsField);

        name.setText("Name: " + profile.getName());
        languagesKnown.setText("Languages Known: " + profile.getLanguagesKnown());
        languagesLearning.setText("Languages Learning: " + profile.getLanguagesLearning());
        interests.setText("Interests: " + profile.getInterests());
    }
}
