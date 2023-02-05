package com.imaduddinsheikh.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    TextView title;

    TextView text;

    TextView dateTime;

    NotesViewHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.title);
        this.dateTime = view.findViewById(R.id.dateTime);
        this.text = view.findViewById(R.id.text);
    }
}
