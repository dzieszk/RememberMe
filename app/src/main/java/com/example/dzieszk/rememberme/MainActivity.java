package com.example.dzieszk.rememberme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<>();
    private NoteDBHelper helper = new NoteDBHelper(this);
    private NoteAdapter adapter;

    public static final int MENU_EDIT = 0;
    public static final int MENU_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecycler();
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.update(helper.getNotes());
    }

    private void setupRecycler(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(helper.getNotes(), helper, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case MENU_EDIT:
                Intent intent = new Intent(this, NoteActivity.class);
                Note note = helper.getNote(item.getOrder());
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.putExtra("image", note.getImage());
                intent.putExtra("edit", "true");
                startActivity(intent);
                break;
            case MENU_DELETE:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.delete)
                        .setMessage(getString(R.string.delete_confirm))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            helper.removeNote(item.getOrder());
                            adapter.update(helper.getNotes());
                        })
                        .setNegativeButton(getString(R.string.nope), null)
                        .show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.backupmenu, menu);
        return true;
    }

    public void backup(MenuItem item){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_save)
                .setTitle(R.string.backup)
                .setMessage(getString(R.string.backup_confirm))
                .setPositiveButton(getString(R.string.yes), ((dialogInterface, i) -> {
                    try {
                        File myFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath()+"/backup.txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        ArrayList<Note> notes = helper.getNotes();
                        for(int j=0; j < helper.size(); j++){
                            myOutWriter.write("Title: \n");
                            myOutWriter.write(notes.get(j).getTitle() + "\n");
                            myOutWriter.write("Content: \n");
                            myOutWriter.write(notes.get(j).getContent() + "\n");
                            if(notes.get(j).getImage() != null) {
                                myOutWriter.write("Image path: \n");
                                myOutWriter.write(notes.get(j).getImage());
                            }
                            myOutWriter.write("\n");
                        }
                        myOutWriter.close();
                        fOut.close();
                        Toast.makeText(this, myFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }))
                .setNegativeButton(getString(R.string.nope), null)
                .show();
    }

    private File createTextFile() throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = File.createTempFile("backup",".txt", storageDir);
        return file;
    }
}