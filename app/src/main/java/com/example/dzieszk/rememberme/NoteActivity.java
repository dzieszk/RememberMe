package com.example.dzieszk.rememberme;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class NoteActivity extends AppCompatActivity {

    private int id;
    private String title;
    private String content;
    private String image;
    private KeyListener listenerTitle;
    private KeyListener listenerContent;
    private NoteDBHelper helper = new NoteDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        this.id = getIntent().getIntExtra("id", 0);
        this.title = getIntent().getStringExtra("title");
        this.content = getIntent().getStringExtra("content");
        this.image = getIntent().getStringExtra("image");

        EditText editText = findViewById(R.id.noteTitle);
        editText.setText(title);
        listenerTitle = editText.getKeyListener();
        editText.setKeyListener(null);
        editText = findViewById(R.id.noteContent);
        editText.setText(content);
        listenerContent = editText.getKeyListener();
        editText.setKeyListener(null);

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

        if(getIntent().getStringExtra("edit") != null){
            EditText title = findViewById(R.id.noteTitle);
            title.setKeyListener(listenerTitle);
            Toast.makeText(this, getString(R.string.edit_mode), Toast.LENGTH_SHORT).show();

            EditText content = findViewById(R.id.noteContent);
            content.setKeyListener(listenerContent);

            FloatingActionButton button = findViewById(R.id.buttonUpdateNote);
            button.setVisibility(View.VISIBLE);
        }
    }

    public void openPhoto(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("image", image);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void editNote(MenuItem item){
        EditText title = findViewById(R.id.noteTitle);
        title.setKeyListener(listenerTitle);
        Toast.makeText(this, R.string.edit_mode, Toast.LENGTH_SHORT).show();

        EditText content = findViewById(R.id.noteContent);
        content.setKeyListener(listenerContent);

        FloatingActionButton button = findViewById(R.id.buttonUpdateNote);
        button.setVisibility(View.VISIBLE);
    }

    public void updateNote(View view) {
        EditText title = findViewById(R.id.noteTitle);
        EditText content = findViewById(R.id.noteContent);
        if(title.getText().toString().length() == 0){
            title.setError(getString(R.string.title_error));
            return;
        }
        if(content.getText().toString().length() == 0){
            content.setError(getString(R.string.content_error));
            return;
        }

        helper.updateNote(id, title.getText().toString(), content.getText().toString());
        title.setKeyListener(null);
        content.setKeyListener(null);
        FloatingActionButton button = findViewById(R.id.buttonUpdateNote);
        button.setVisibility(View.GONE);
        Toast.makeText(this, R.string.view_mode, Toast.LENGTH_SHORT).show();
    }
}
