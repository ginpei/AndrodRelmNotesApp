package info.ginpei.notes.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {
    @PrimaryKey
    private long id = 0;

    private Date createdAt;

    private Date updatedAt;

    @NonNull
    private String comment = "";

    @Nullable
    private String photoFilePath = null;

    @Nullable
    private String photoThumbFilePath = null;

    // vvvvvvvv

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt() {
        setUpdatedAt(new Date());
    }

    @NonNull
    public String getComment() {
        return comment;
    }

    public void setComment(@NonNull String comment) {
        this.comment = comment;
    }

    public void setComment(@NonNull Realm realm, @NonNull String comment) {
        realm.beginTransaction();
        setComment(comment);
        setUpdatedAt();
        realm.commitTransaction();
    }

    @Nullable
    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setPhotoFilePath(@Nullable String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public void setPhotoFilePath(Realm realm, @Nullable String photoFilePath) {
        realm.beginTransaction();
        setPhotoFilePath(photoFilePath);
        setUpdatedAt();
        realm.commitTransaction();
    }

    @Nullable
    public String getPhotoThumbFilePath() {
        return photoThumbFilePath;
    }

    public void setPhotoThumbFilePath(@Nullable String photoThumbFilePath) {
        this.photoThumbFilePath = photoThumbFilePath;
    }
    // ^^^^^^^^


    public Note() {
    }

    public void save(Realm realm) {
        Date now = new Date();
        setUpdatedAt(now);

        boolean isNew = this.getId() < 1;
        if (isNew) {
            long id = findLastId(realm) + 1;
            setId(id);

            setCreatedAt(now);
        }

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(this);
        realm.commitTransaction();
    }

    public void delete(Realm realm) {
        realm.beginTransaction();
        String path = getPhotoFilePath();
        deleteFromRealm();
        deleteFiles(path);
        realm.commitTransaction();
    }

    private void deleteFiles(String path) {
        if (path != null) {
            new File(path).delete();
        }
    }

    private static long findLastId(Realm realm) {
        RealmQuery<Note> query = realm.where(Note.class);
        if (query.count() > 0) {
            return query.max("id").longValue();
        } else {
            return 0;
        }
    }

    public static void findAllForList(Realm realm, ArrayList<Note> notes) {
        RealmResults<Note> results = realm.where(Note.class).findAllSorted("updatedAt", Sort.DESCENDING);
        notes.clear();
        notes.addAll(results);
    }

    @Nullable
    public static Note find(Realm realm, long noteId) {
        return realm.where(Note.class).equalTo("id", noteId).findFirst();
    }
}
