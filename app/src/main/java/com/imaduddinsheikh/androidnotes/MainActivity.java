package com.imaduddinsheikh.androidnotes;

import android.app.Activity;
import android.content.Context;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;
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
        BorderItemDecoration spacingItemDecoration = new BorderItemDecoration(this, 13, 5);
        recyclerView.addItemDecoration(spacingItemDecoration);

        if (savedInstanceState != null) {
            noteList = (List<Note>) savedInstanceState.getSerializable("NOTE_LIST");
            adapter.notifyDataSetChanged();
            linearLayoutManager.scrollToPosition(0);
            changeTitleIfNeeded();
            super.onRestoreInstanceState(savedInstanceState);
        }

        changeTitleIfNeeded();

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
                noteList.remove(old);
                adapter.notifyItemRemoved(posClicked);
            }
            noteList.add(0, n);
            adapter.notifyItemInserted(0);
            linearLayoutManager.scrollToPosition(0);
        } else {
            Toast.makeText(this, "OTHER result not OK!", Toast.LENGTH_SHORT).show();
        }
        changeTitleIfNeeded();
        saveNotes();
        Log.d(TAG, "noteList size: " + noteList.toString());
        posClicked = Integer.MIN_VALUE;
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
            addTop();
            changeTitleIfNeeded();
            return true;
        } else if (id == R.id.menuAbout) {
            Intent intent = new Intent(this, AboutActivity.class);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", (dialog, id) -> {
            noteList.remove(pos);
            adapter.notifyItemRemoved(pos);
            changeTitleIfNeeded();
            saveNotes();
            Log.d(TAG, "noteList size: " + noteList.toString());
        });
        builder.setNegativeButton("NO", (dialog, id) -> dialog.dismiss());
        builder.setTitle("Delete Note '" + noteList.get(pos).getTitle() + "'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setPositiveButton("YES", (dialog, id1) -> {
            finish();
        });
        builder2.setNegativeButton("NO", (dialog, id2) -> {
            dialog.dismiss();
        });
        builder2.setTitle("Are you sure you want to exit?");
        AlertDialog dialog = builder2.create();
        dialog.show();
    }

    public void addTop() {
        Intent intent = new Intent(this, EditActivity.class);
        activityResultLauncher.launch(intent);
    }

    public void changeTitleIfNeeded() {
        if (adapter.getItemCount() > 0) {
            setTitle(getString(R.string.app_name) + " (" + adapter.getItemCount() + ")");
        } else {
            setTitle(getString(R.string.app_name));
        }
    }

    private void saveNotes() {

        Log.d(TAG, "saveNotes: Saving JSON File");

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            JSONArray jsonArray = new JSONArray();
            for (Note note : noteList) {
                JSONObject jsonObject = note.toJSON();
                jsonArray.put(jsonObject);
            }

            Log.d(TAG, "saveNotes: " + jsonArray);
            printWriter.print(jsonArray);
            printWriter.close();
            fos.close();
            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onPause() { // Going to be partially or fully hidden
        super.onPause();
    }

    private void loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            FileInputStream fis = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String text = jsonObject.getString("text");
                long lastUpdatedDateTime = jsonObject.getLong("lastUpdatedDateTime");

                Note note = new Note(title, text, lastUpdatedDateTime);
                noteList.add(note);
                Log.d(TAG, "loadFile: " + noteList);
            }
            reader.close();
            fis.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() { // After Pause or Stop
        noteList.clear();
        loadFile();
        changeTitleIfNeeded();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("NOTE_LIST", (Serializable) noteList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        noteList = (List<Note>) savedInstanceState.getSerializable("NOTE_LIST");
        adapter = new NotesAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onRestoreInstanceState: " + noteList.toString());
    }
}