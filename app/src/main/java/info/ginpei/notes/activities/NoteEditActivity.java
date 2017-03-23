package info.ginpei.notes.activities;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import info.ginpei.notes.BR;
import info.ginpei.notes.R;
import info.ginpei.notes.databinding.ActivityNoteEditBinding;
import info.ginpei.notes.models.Note;
import io.realm.Realm;

public class NoteEditActivity extends AppCompatActivity {

    public static final String TAG = "G#NoteEditActivity";
    private Realm realm;
    private Note note;
    public ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_note_edit);

        restoreNote();

        // shouldn't be, but just in case
        if (note == null) {
            Toast.makeText(this, "Unknown note.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        vm = new ViewModel();
        vm.setComment(note.getComment());

        ActivityNoteEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_note_edit);
        binding.setVm(vm);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_noteedit, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isExistingNote = note.getId() > 0;
        if (isExistingNote) {
            MenuItem item = menu.findItem(R.id.action_saveNote);
            item.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_saveNote) {
            saveAndFinish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveAndFinish() {
        Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();
    }

    public class ViewModel extends BaseObservable {

        public String comment = "";

        @Bindable
        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
            notifyPropertyChanged(BR.comment);
        }

        public void comment_textChanged(CharSequence charSequence, int i, int i1, int i2) {
            String comment = charSequence.toString();
            note.setComment(realm, comment);
        }
    }
}
