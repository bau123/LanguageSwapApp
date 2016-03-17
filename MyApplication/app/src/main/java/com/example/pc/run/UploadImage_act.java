package com.example.pc.run;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pc.run.Global.GlobalBitmap;

import java.io.IOException;

public class UploadImage_act extends AppCompatActivity implements View.OnClickListener {

    private Button chooseImage;
    private Button buttonReturn;
    private ImageView imageView;
    private Bitmap bitmap;
    String email;

    private int request = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_act);

        email = getIntent().getStringExtra("email");

        chooseImage = (Button) findViewById(R.id.buttonChoose);
        buttonReturn = (Button) findViewById(R.id.buttonReturn);

        imageView = (ImageView) findViewById(R.id.imageView);

        chooseImage.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
    }


    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri file = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if(v==chooseImage){
            showFileChooser();
        }

        if(v==buttonReturn){
            Intent intent = new Intent(UploadImage_act.this, CreateProfile_Act.class);
            GlobalBitmap.bitmap = bitmap;
            intent.putExtra("email", email);
            startActivity(intent);
        }
    }

}

