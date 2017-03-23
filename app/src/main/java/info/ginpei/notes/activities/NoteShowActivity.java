package info.ginpei.notes.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import info.ginpei.notes.R;
import info.ginpei.notes.models.Note;
import io.realm.Realm;

public class NoteShowActivity extends AppCompatActivity {

    private Realm realm;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        restoreNote();

        setContentView(R.layout.activity_note_show);

        Toast.makeText(this, note.getComment(), Toast.LENGTH_SHORT).show();
    }

    private void restoreNote() {
        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", -1);
        if (noteId < 0) {
            note = new Note();
        } else {
            note = Note.find(realm, noteId);
        }
    }
}
