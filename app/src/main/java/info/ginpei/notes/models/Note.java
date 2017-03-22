package info.ginpei.notes.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.realm.annotations.PrimaryKey;

public class Note {
    @PrimaryKey
    private long id;

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

    public String getProperTitle() {
        String title = getTitle();
        if (title.isEmpty()) {
            title = getBody();
        }
        return title;
    }

    public static Note create() {
        return new Note();
    }

    public static ArrayList<Note> findAll() {
        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Note note = new Note();
            note.setBody("Note #" + i);
            notes.add(note);
        }
        return notes;
    }
}
