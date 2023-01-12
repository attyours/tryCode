package com.example.project3.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project3.R;
import com.example.project3.UpdateActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import db.DbHelper;
import model.Notes;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder>{
    private ArrayList<Notes> notesArrayList= new ArrayList<>();
    private Activity activity;
    private DbHelper dbHelper;
    private Timer timer;
    private List<Notes> notesSource;
    LinearLayout layoutNote;

    public NotesAdapter(Activity activity){
        this.activity = activity;
        dbHelper= new DbHelper(activity);
    }

    public ArrayList<Notes> getNotesArrayList(){
        return notesArrayList;
    }

    public void setNotesArrayList(ArrayList<Notes> notesArrayList){
        try {
            if (notesArrayList.size() >0){
                this.notesArrayList.clear();
            }
            this.notesArrayList.addAll(notesArrayList);
            notifyDataSetChanged();
        } catch (Exception e){
            System.out.println("error on "+e);
        }

    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notes,
                parent,false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.tvTitle.setText(notesArrayList.get(position).getTitle());
        holder.tvSubtitle.setText(notesArrayList.get(position).getSubtitle());
        holder.tvDates.setText(notesArrayList.get(position).getDateTime());
        holder.setNote(notesArrayList.get(position));
        byte[] bytes = notesArrayList.get(position).getImage();

        if(bytes != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imageNotes.setImageBitmap(bitmap);
            holder.imageNotes.setVisibility(View.VISIBLE);
        }
        holder.noteLayout.setOnClickListener((View v)->{
            Intent intent = new Intent(activity, UpdateActivity.class);
            intent.putExtra("note", (Serializable) notesArrayList.get(position));
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesArrayList.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder{
        final TextView tvTitle,tvSubtitle,tvDates;
        final LinearLayout noteLayout;
        final RoundedImageView imageNotes;
        public NotesViewHolder(@NonNull View itemView){
            super(itemView);
            tvTitle=itemView.findViewById(R.id.titleNote);
            tvSubtitle=itemView.findViewById(R.id.subtitleNote);
            tvDates=itemView.findViewById(R.id.textDate);
            noteLayout= itemView.findViewById(R.id.cvNotes);
            imageNotes=itemView.findViewById(R.id.imageNoteItem);
        }
        void setNote(Notes notes){
            GradientDrawable gradientDrawable = (GradientDrawable) noteLayout.getBackground();
            if(notes.getColor()!=null){
                gradientDrawable.setColor(Color.parseColor(notes.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#80171C26"));
            }
        }
    }

}
