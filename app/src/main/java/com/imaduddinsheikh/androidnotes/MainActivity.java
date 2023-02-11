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
            if (data.hasExtra("NOTE")) {
                Note newNote = (Note) data.getSerializableExtra("NOTE");
                if (newNote != null) {
                    noteList.add(0, newNote);
                    adapter.notifyItemInserted(0);
                    setTitle("Android Notes (" + noteList.size() + ")");
                    linearLayoutManager.scrollToPosition(0);
                }
            }
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
            Intent intent = new Intent(this, AddNoteActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);
        Toast.makeText(v.getContext(), "SHORT " + n.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = noteList.get(pos);
        Toast.makeText(v.getContext(), "LONG " + n.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}