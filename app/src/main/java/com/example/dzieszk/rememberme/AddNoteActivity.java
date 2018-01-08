package com.example.dzieszk.rememberme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {

    private NoteDBHelper helper = new NoteDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
    }

    public void saveNote(View view) {
        EditText title = findViewById(R.id.noteTitle);
        EditText content = findViewById(R.id.noteContent);
        helper.addNote(title.getText().toString(), content.getText().toString());
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }
}
