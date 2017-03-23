package info.ginpei.notes.activities;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import info.ginpei.notes.BR;
import info.ginpei.notes.R;
import info.ginpei.notes.databinding.ActivityNoteShowBinding;
import info.ginpei.notes.models.Note;
import info.ginpei.notes.utils.NiceDateFormat;
import info.ginpei.notes.views.GoogleMapImageView;
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
        vm.setDateText(NiceDateFormat.format(note.getCreatedAt()));
        vm.setComment(note.getComment());
        vm.setPhotoPath(note.getPhotoFilePath());
        vm.setLocationName(note.getLocationName());

        setContentView(R.layout.activity_note_edit);

        ActivityNoteShowBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_note_show);
        binding.setVm(vm);

        GoogleMapImageView mapImageView = (GoogleMapImageView) findViewById(R.id.image_map);
        mapImageView.setLocation(this, note.getLatitude(), note.getLongitude());
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

        @NonNull
        private String dateText = "";

        @Bindable
        @NonNull
        public String getDateText() {
            return dateText;
        }

        public void setDateText(@NonNull String dateText) {
            this.dateText = dateText;
            notifyPropertyChanged(BR.dateText);
        }

        public String comment = "";

        @Bindable
        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
            notifyPropertyChanged(BR.comment);
        }

        @NonNull
        private String photoPath = "";

        @Bindable
        @NonNull
        public String getPhotoPath() {
            return photoPath;
        }

        public void setPhotoPath(@NonNull String photoPath) {
            this.photoPath = photoPath;
            notifyPropertyChanged(BR.photoPath);
        }

        @NonNull
        private String locationName = "";

        @Bindable
        @NonNull
        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(@NonNull String locationName) {
            this.locationName = locationName;
            notifyPropertyChanged(BR.locationName);
        }
    }
}
