package info.ginpei.notes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.ginpei.notes.BR;
import info.ginpei.notes.R;
import info.ginpei.notes.databinding.ActivityNoteEditBinding;
import info.ginpei.notes.models.Note;
import io.realm.Realm;

public class NoteEditActivity extends BaseLocationActivity {

    public static final String TAG = "G#NoteEditActivity";
    public static final int REQUEST_TAKE_PHOTO = 1;
    private Realm realm;
    private Note note;
    public ViewModel vm;
    private String photoFileAbsolutePath;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                setPhoto(photoFileAbsolutePath);
                photoFileAbsolutePath = null;
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file = createImageFIle();
            if (file != null) {
                photoFileAbsolutePath = file.getAbsolutePath();

                String authority = getPackageName() + ".fileprovider";
                Uri outputUri = FileProvider.getUriForFile(this, authority, file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                startActivityForResult(intent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFIle() {
        String timestamp = getTimestamp();
        File image = null;
        try {
            image = File.createTempFile(
                    "JPEG_" + timestamp + "_",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            );
        } catch (IOException ignored) {
        }
        return image;
    }

    @SuppressLint("SimpleDateFormat")
    private String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private void setPhoto(@NonNull String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        ImageView imageView = (ImageView) findViewById(R.id.image_photo);
        imageView.setImageBitmap(bitmap);

        note.setPhotoFilePath(path);

        createPhotoThumbnail(path);
    }

    private void saveAndFinish() {
        note.save(realm);
        finish();
    }

    private void createPhotoThumbnail(@NonNull String originalPath) {
        final float PHOTO_MAX_WIDTH = getResources().getDimension(R.dimen.row_home_notes_image_width);

        String path = originalPath + ".thumb.jpg";

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originalPath, options);  // update options by original photo
        options.inJustDecodeBounds = false;

        options.inSampleSize = (int) (options.outWidth / PHOTO_MAX_WIDTH);

        Bitmap bitmap = BitmapFactory.decodeFile(originalPath, options);

        FileOutputStream out = null;
        boolean failed = false;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            failed = true;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    failed = true;
                }
            }
        }

        if (!failed) {
            note.setPhotoThumbFilePath(path);
        }
    }

    public class ViewModel extends BaseObservable {

        @Bindable
        public boolean isNewNote() {
            return note.getId() == 0;
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

        public void comment_textChanged(CharSequence charSequence, int i, int i1, int i2) {
            String comment = charSequence.toString();
            note.setComment(realm, comment);
        }

        public void save_click(View view) {
            saveAndFinish();
        }

        public void takePhoto_click(View view) {
            takePhoto();
        }
    }
}
