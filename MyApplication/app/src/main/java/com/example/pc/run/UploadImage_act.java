package com.example.pc.run;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pc.run.Global.GlobalBitmap;
import com.example.pc.run.Network_Utils.Requests;
import com.example.pc.run.SharedPref.ApplicationSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImage_act extends AppCompatActivity implements View.OnClickListener {

    private Button chooseImage;
    private Button uploadImage;
    private Button buttonReturn;
    private ImageView imageView;
    private Bitmap bitmap;
    String email;

    private int request = 1;
    private String UPLOAD_URL = "http://k1.esy.es/insert-db-image.php";

    private String IMAGE_KEY = "image";
    private String EMAIL_KEY = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_act);

        email = getIntent().getStringExtra("email");

        chooseImage = (Button) findViewById(R.id.buttonChoose);
        uploadImage = (Button) findViewById(R.id.buttonUpload);
        buttonReturn = (Button) findViewById(R.id.buttonReturn);

        imageView = (ImageView) findViewById(R.id.imageView);

        chooseImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        buttonReturn.setOnClickListener(this);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {

        final ProgressDialog loading = ProgressDialog.show(this, "Uploading...",
                "Please wait...", false, false);

        String image = getStringImage(bitmap);
        Log.d("IMAGE", image);

        Map<String, String> params = new HashMap<>();

        params.put(IMAGE_KEY, image);
        params.put(EMAIL_KEY, email);

        Requests jsObjRequest = new Requests(Request.Method.POST, UPLOAD_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.dismiss();
                    System.out.println(response.toString());
                    processResult(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
                loading.dismiss();
            }
        });
        ApplicationSingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    private void processResult(JSONObject input) throws InterruptedException {
        String result = "";
        try{
            result = input.getString("message");
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (result.equals("success")) {

            System.out.println("SUCCESS");
        } else if (result.equals("failure")) {

            System.out.println("FAILURE");
        }

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

        if(v==uploadImage){
            uploadImage();
        }

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

