package com.example.dzieszk.rememberme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private NoteDBHelper helper = new NoteDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper.addNote("jaiiś tytuł", "jakiś content");
        Log.d("XD", helper.getNote("jaiiś tytuł").getTitle());
    }
}
