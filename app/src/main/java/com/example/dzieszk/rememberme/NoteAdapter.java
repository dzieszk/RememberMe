package com.example.dzieszk.rememberme;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dzieszk on 08.01.18.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private ArrayList<Note> notes;
    private NoteDBHelper helper;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int id;
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

    public NoteAdapter(ArrayList<Note> notes, NoteDBHelper helper, Context context){
        this.context = context;
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
        holder.id = notes.get(position).getId();
        holder.title.setText(notes.get(position).getTitle());
        holder.content.setText(notes.get(position).getContent());

        holder.itemView.setOnClickListener(view -> {
            Note note = helper.getNote(holder.id);
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("id", note.getId());
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("image", note.getImage());
            context.startActivity(intent);
        });

        holder.itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(Menu.NONE, MainActivity.MENU_EDIT, holder.id, R.string.edit);
            contextMenu.add(Menu.NONE, MainActivity.MENU_DELETE, holder.id, R.string.delete);
        });

        if(notes.get(position).getImage() == null) {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getColor(holder.title.getText());
            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(holder.title.getText().charAt(0)), color);
            holder.image.setImageDrawable(drawable);
        }
        else{
            File file = new File(notes.get(position).getImage());
            if(file.exists()){
                int targetW = 48;
                int targetH = 48;
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(notes.get(position).getImage(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
                holder.image.setImageBitmap(BitmapFactory.decodeFile(notes.get(position).getImage(), bmOptions));
            }
            else{
                holder.image.setImageResource(R.drawable.ic_error_black_24dp);
            }
        }
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
