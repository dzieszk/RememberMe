package com.example.dzieszk.rememberme;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private NoteDBHelper helper = new NoteDBHelper(this);
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
    }

    public void saveNote(View view) {
        EditText title = findViewById(R.id.addTitle);
        EditText content = findViewById(R.id.addContent);
        ImageView photo = findViewById(R.id.addPhoto);
        if(title.getText().toString().length() == 0){
            title.setError("Tytuł nie może być pusty!");
            return;
        }
        if(content.getText().toString().length() == 0){
            content.setError("Notatka nie może być pusta");
            return;
        }
        if(photo.getDrawable() != null){
            helper.addNote(title.getText().toString(), content.getText().toString(), mCurrentPhotoPath);
        }
        else {
            helper.addNote(title.getText().toString(), content.getText().toString());
        }
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }

    public void addPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException ex){
                //TODO ERROR
                Log.d("Error", "IOEXCEPTION");
            }
            if(photoFile != null){
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            ImageView imageView = findViewById(R.id.addPhoto);
            File imgFile = new File(mCurrentPhotoPath);
            if(imgFile.exists()){
                imageView.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private File createImageFile() throws IOException{
       String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
       String imageFilename = "JPEG_" + timestamp;
       File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       File image = File.createTempFile(imageFilename,".jpg", storageDir);
       mCurrentPhotoPath = image.getAbsolutePath();
       return image;
    }

}
