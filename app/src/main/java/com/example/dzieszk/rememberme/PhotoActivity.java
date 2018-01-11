package com.example.dzieszk.rememberme;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        image = getIntent().getStringExtra("image");

        if(this.image != null){
            ImageView imageView = findViewById(R.id.fullPhoto);
            File file = new File(image);
            if(file.exists()){
                imageView.setImageBitmap(BitmapFactory.decodeFile(image));
            }
        }
    }
}
