package info.ginpei.notes.models;

import android.support.annotation.NonNull;

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
    private String title = "";

    @NonNull
    private String body = "";

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

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getBody() {
        return body;
    }

    public void setBody(@NonNull String body) {
        this.body = body;
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

    public String getProperTitle() {
        String title = getTitle();
        if (title.isEmpty()) {
            title = getBody();
        }
        return title;
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
}
