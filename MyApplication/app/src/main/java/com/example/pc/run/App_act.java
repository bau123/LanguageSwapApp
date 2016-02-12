package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class App_act extends AppCompatActivity {

    Button chatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_act);

        chatBtn = (Button) findViewById(R.id.enterChat);
    }
}
