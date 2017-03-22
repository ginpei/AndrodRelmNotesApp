package info.ginpei.notes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import info.ginpei.notes.R;

public class NoteEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", -1);
        Toast.makeText(this, "ID=" + noteId, Toast.LENGTH_SHORT).show();
    }
}
