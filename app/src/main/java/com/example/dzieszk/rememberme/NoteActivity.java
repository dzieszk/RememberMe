package com.example.dzieszk.rememberme;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class NoteActivity extends AppCompatActivity {

    private String title;
    private String content;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        this.title = getIntent().getStringExtra("title");
        this.content = getIntent().getStringExtra("content");
        this.image = getIntent().getStringExtra("image");

        EditText editText = findViewById(R.id.noteTitle);
        editText.setText(title);
        editText.setFocusable(false);
        editText = findViewById(R.id.noteContent);
        editText.setText(content);
        editText.setFocusable(false);

        if(this.image != null){
            ImageView imageView = findViewById(R.id.photo);
            File file = new File(image);
            if(file.exists()){
                int targetW = 192;
                int targetH = 192;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(image, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                imageView.setImageBitmap(BitmapFactory.decodeFile(image, bmOptions));
                imageView.setVisibility(View.VISIBLE);
            }
        }
        else{
            ImageView imageView = findViewById(R.id.photo);
            imageView.setImageResource(R.drawable.ic_error_black_24dp);
        }
    }

    public void openPhoto(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("image", image);
        startActivity(intent);
    }
}
