package info.ginpei.notes.activities;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

        Intent intent = getIntent();
        long noteId = intent.getLongExtra("noteId", -1);
        note = Note.find(realm, noteId);

        vm = new ViewModel();
        vm.setComment(note.getComment());

        ActivityNoteEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_note_edit);
        binding.setVm(vm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
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
