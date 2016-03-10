package com.example.pc.run.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.pc.run.R;

import java.util.List;

public class chat extends AppCompatActivity {

    Button send;
    ListView viewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        send = (Button) findViewById(R.id.sendBtn);
        viewMessages = (ListView) findViewById(R.id.viewChat);
    }

    public void send(View view){

    }

}
