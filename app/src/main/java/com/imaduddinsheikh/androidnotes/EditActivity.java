package com.imaduddinsheikh.androidnotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";

    private EditText noteTitle;

    private EditText noteText;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_note);

        noteTitle = findViewById(R.id.noteTitleBox);
        noteText = findViewById(R.id.noteTextBox);

        Intent intent = getIntent();
        if (intent.hasExtra("NOTE")) {
            note = (Note) getIntent().getSerializableExtra("NOTE");
            noteTitle.setText(note.getTitle());
            noteText.setText(note.getText());
        } else {
            note = new Note(noteTitle.getText().toString(), noteText.getText().toString());
        }
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setPositiveButton("OK", (dialog, id1) -> {
                    super.onBackPressed();
                });
                builder1.setNegativeButton("CANCEL", (dialog, id1) -> {
                    dialog.dismiss();
                });
                builder1.setTitle("Your note doesn't have a title.");
                AlertDialog dialog = builder1.create();
                dialog.show();
                return true;
            }

            Intent intent = new Intent();
            Note newNote = new Note(noteTitleText, noteTextText);
            if (!(newNote.getTitle().equals(note.getTitle())) || !(newNote.getText().equals(note.getText()))) {
                intent.putExtra("NOTE", newNote);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                finish();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        String noteTitleText = noteTitle.getText().toString();
        String noteTextText = noteText.getText().toString();

        if ((noteTitleText.trim().isEmpty() && !noteTextText.isEmpty()) || (noteTitleText.trim().isEmpty() && noteTextText.isEmpty())) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setPositiveButton("OK", (dialog, id1) -> {
                super.onBackPressed();
            });
            builder1.setNegativeButton("CANCEL", (dialog, id1) -> {
                dialog.dismiss();
            });
            builder1.setTitle("Your note doesn't have a title.");
            AlertDialog dialog = builder1.create();
            dialog.show();
        } else {
            if (!noteTitleText.trim().equals(note.getTitle()) || !noteTextText.equals(note.getText())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("YES", (dialog, id) -> {
                    onBackPressedSave(noteTitleText, noteTextText);
                });
                builder.setNegativeButton("NO", (dialog, id) -> {
                    super.onBackPressed();
                });
                builder.setTitle("Your note is not saved!\nSave Note '" + noteTitleText + "'?");
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void onBackPressedSave(String noteTitleText, String noteTextText) {
        Intent intent = new Intent();
        Note newNote = new Note(noteTitleText, noteTextText);
        if (!(newNote.getTitle().equals(note.getTitle())) || !(newNote.getText().equals(note.getText()))) {
            intent.putExtra("NOTE", newNote);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("NOTE_TITLE", noteTitle.getText().toString());
        outState.putString("NOTE_TEXT", noteText.getText().toString());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        noteTitle.setText(savedInstanceState.getString("NOTE_TITLE"));
        noteText.setText(savedInstanceState.getString("NOTE_TEXT"));
    }
}