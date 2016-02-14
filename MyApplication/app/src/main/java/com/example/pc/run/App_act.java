package com.example.pc.run;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.pc.run.Listeners.OnSwipeTouchListener;

public class App_act extends AppCompatActivity {

    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_act);

        img = (ImageView)findViewById(R.id.imageView);

        /*
        Basic overriding for implementation - to be implemented for profile views
         */
        img.setOnTouchListener(new OnSwipeTouchListener(App_act.this){

            public void onSwipeTop(){
                img.setImageResource(R.drawable.fiveimg);
            }
            public void onSwipeRight(){
                img.setImageResource(R.drawable.fourimg);
            }
            public void onSwipeLeft(){
                img.setImageResource(R.drawable.twoimg);
            }
            public void onSwipeBottom(){
                img.setImageResource(R.drawable.threeimg);
            }
        });
    }
}

