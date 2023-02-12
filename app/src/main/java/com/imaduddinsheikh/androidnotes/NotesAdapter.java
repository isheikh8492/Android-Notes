package com.imaduddinsheikh.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private static final String TAG = "NotesAdapter";

    private final List<Note> noteList;

    private final MainActivity mainActivity;

    NotesAdapter(List<Note> notesList, MainActivity ma) {
        this.noteList = notesList;
        this.mainActivity = ma;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW NoteViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note " + position);

        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd, h:mm a");
        String dateTime = simpleDateFormat.format(new Date(note.getLastUpdateDateTime()));
        holder.dateTime.setText(dateTime);
        holder.text.setText(note.getText());
        if (note.getText().length() > 80) {
            String truncatedText = note.getText();
            truncatedText = truncatedText.substring(0, 80) + "...";
            holder.text.setText(truncatedText);
        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}