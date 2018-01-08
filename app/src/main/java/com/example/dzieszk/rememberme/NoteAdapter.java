package com.example.dzieszk.rememberme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dzieszk on 08.01.18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Note> notes;
    private NoteDBHelper helper;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public ImageView image;

        public ViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            content = itemView.findViewById(R.id.item_content);
            image = itemView.findViewById(R.id.item_image);
        }
    }

    public NoteAdapter(ArrayList<Note> notes, NoteDBHelper helper){
        this.notes = notes;
        this.helper = helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.title.setText(notes.get(position).getTitle());
        holder.content.setText(notes.get(position).getContent());
    }

    @Override
    public int getItemCount(){
        return notes.size();
    }

    public void update(ArrayList<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
}
