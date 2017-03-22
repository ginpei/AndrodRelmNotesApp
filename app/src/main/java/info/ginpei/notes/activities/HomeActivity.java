package info.ginpei.notes.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import info.ginpei.notes.R;
import info.ginpei.notes.models.Note;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = "G#HomeActivity";

    private Realm realm;
    private final ArrayList<Note> notes = new ArrayList<>();
    private ArrayAdapter<Note> notesAdapter;
    private ListView notesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        reloadNotes();

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewNote();
            }
        });

        initNotesView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reloadNotes() {
        Note.findAllForList(realm, notes);
    }

    private void initNotesView() {
        notesAdapter = new NoteArrayAdapter(this, notes);

        notesView = (ListView) findViewById(R.id.list_notes);
        notesView.setAdapter(notesAdapter);
    }

    private void createNewNote() {
        Note note = new Note();
        note.setTitle("New note");
        note.save(realm);

        reloadNotes();
        notesAdapter.notifyDataSetChanged();
    }

    class NoteArrayAdapter extends ArrayAdapter<Note> {
        public NoteArrayAdapter(@NonNull Context context, ArrayList<Note> notes) {
            super(context, android.R.layout.simple_list_item_2, android.R.id.text1, notes);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Note note = notes.get(position);
            String title = note.getProperTitle();
            String body = note.getBody();

            View view = super.getView(position, convertView, parent);
            ((TextView) view.findViewById(android.R.id.text1)).setText(title);
            ((TextView) view.findViewById(android.R.id.text2)).setText(body);
            return view;
        }
    }
}
