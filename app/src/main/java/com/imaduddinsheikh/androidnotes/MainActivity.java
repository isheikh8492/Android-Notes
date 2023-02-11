package com.imaduddinsheikh.androidnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private final List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;

    private NotesAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private static final int EDIT_NOTE_REQUEST = 123;

    private static final int NEW_NOTE_REQUEST = 124;

    private int posClicked = Integer.MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.notesRecyclerView);

        adapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < 30; i++) {
            noteList.add(new Note("Hello " + i, "jndfkndjndfkj"));
        }

        if (noteList.size() > 0) {
            setTitle(getTitle() + " (" + noteList.size() + ")");
        }

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    public void handleResult(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == Activity.RESULT_OK) {
            Note n = (Note) data.getSerializableExtra("NOTE");

            if (posClicked != RecyclerView.NO_POSITION && posClicked >= 0 && posClicked < noteList.size()) {
                Note old = noteList.get(posClicked);
                old.setTitle(n.getTitle());
                old.setText(n.getText());
                noteList.add(0, old);
                adapter.notifyItemChanged(0);
            } else {
                noteList.add(0, n);
                adapter.notifyItemInserted(0);
            }
            linearLayoutManager.scrollToPosition(0);
            setTitle("Android Notes (" + noteList.size() + ")");
        } else {
            Toast.makeText(this, "OTHER result not OK!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAddNote) {
            Intent intent = new Intent(this, EditActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        posClicked = recyclerView.getChildLayoutPosition(v);

        Note n = noteList.get(posClicked);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE", n);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + n.toString(), Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    public void AddNewNote(View view) {

    }
}