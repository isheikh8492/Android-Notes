package com.imaduddinsheikh.androidnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";

    private EditText noteTitle;

    private EditText noteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_note);

        noteTitle = findViewById(R.id.noteTitleBox);
        noteText = findViewById(R.id.noteTextBox);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.add_new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuSave) {
            String noteTitleText = noteTitle.getText().toString();
            String noteTextText = noteText.getText().toString();
            if (noteTitleText.trim().isEmpty()) {
                Toast.makeText(this, "Please enter the note's title", Toast.LENGTH_SHORT).show();
                return true;
            } else if (noteTextText.trim().isEmpty()) {
                Toast.makeText(this, "Please enter the note's text", Toast.LENGTH_SHORT).show();
                return true;
            }

            Intent intent = new Intent();
            Note newNote = new Note(noteTitleText, noteTextText);
            intent.putExtra("NOTE", newNote);
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}