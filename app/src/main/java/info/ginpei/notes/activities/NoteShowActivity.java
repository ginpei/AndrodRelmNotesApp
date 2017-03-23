package info.ginpei.notes.activities;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import info.ginpei.notes.BR;
import info.ginpei.notes.R;
import info.ginpei.notes.databinding.ActivityNoteShowBinding;
import info.ginpei.notes.models.Note;
import io.realm.Realm;

public class NoteShowActivity extends AppCompatActivity {

    private Realm realm;
    private Note note;
    private ViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        restoreNote();

        setContentView(R.layout.activity_note_show);

        vm = new ViewModel();
        vm.setComment(note.getComment());

        setContentView(R.layout.activity_note_edit);

        ActivityNoteShowBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_note_show);
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

        private String locationName = "";
    }
}
