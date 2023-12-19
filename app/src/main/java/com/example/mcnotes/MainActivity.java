package com.example.mcnotes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private LinearLayout notesContainer;
    private List<Note> noteList;
    private DatabaseHelper userDb = new DatabaseHelper(MainActivity.this);
    ArrayList<String> noteTitle, notecontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        noteList = new ArrayList<>();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        // loadNotesFromPreferences();
        displayNotes();
    }


    private void displayNotes() {
        Cursor cursor = userDb.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();

        } else {

            while (cursor.moveToNext()) {

                String title = cursor.getString(1);
                String content = cursor.getString(2);

                Note note = new Note();
                note.setTitle(title);
                note.setContent(content);
                note.ID=cursor.getInt(0);
                noteList.add(note);
                createNoteView(note);
            }
        }
    }

    private void saveNote() {

        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();


        if (!title.isEmpty() && !content.isEmpty()) {

            userDb.addNote(titleEditText.getText().toString().trim(), contentEditText.getText().toString().trim());
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            noteList.add(note);


            createNoteView(note);
            clearInputFields();
        }
    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);
    }

    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteNoteAndRefresh(note);
            }
        });

        builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Title = note.getTitle();
                String Content = note.getContent();
                View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
                EditText titleEditText = findViewById(R.id.titleEditText);
                EditText contentEditText = findViewById(R.id.contentEditText);
                titleEditText.setText(Title);
                contentEditText.setText(Content);
                userDb.deleteOneRow(note.ID);
                userDb.updateData(note.ID, Title, Content);


                int noteIndex = noteList.indexOf(note);
                noteList.remove(noteIndex);

                refreshNoteViews();

            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note) {


        int noteIndex = noteList.indexOf(note);
        userDb.deleteOneRow(note.ID);

        noteList.remove(noteIndex);

        refreshNoteViews();

    }

    private void refreshNoteViews() {
        notesContainer.removeAllViews();
        displayNotes();
    }
}
