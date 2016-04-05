package com.austinjones.whenitswrite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Firebase mFirebaseRootRef;
    private Firebase mSongDeleteUrl;

    private String mUserId;
    private String songUrl;
    ArrayList<DataSnapshot> mSongs;
    DataSnapshot dataSnapshot;
    TextView resultText;
    EditText songTitle;
    Song songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check Authentication
        mFirebaseRootRef = new Firebase("https://whenitswrite.firebaseio.com");
        if (mFirebaseRootRef.getAuth() == null) {
            loadLoginView();
        }

        try {
            mUserId = mFirebaseRootRef.getAuth().getUid();
        } catch (Exception e) {
            loadLoginView();
        }

        songUrl = mFirebaseRootRef + "/data/" + mUserId + "/songs";



        // Set up ListView
        final ListView songList = (ListView) findViewById(R.id.songList);
        mSongs = new ArrayList();
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        //final ArrayAdapter<Song> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        songList.setAdapter(adapter);

        final CustomAdapter titleAdapter = new CustomAdapter(this, mSongs);
        songList.setAdapter(titleAdapter);

        // Add items via the Button and EditText at the bottom of the view.
        songTitle = (EditText) findViewById(R.id.songTitle);
        final Button button = (Button) findViewById(R.id.addButton);
        final String titleText = songTitle.getText().toString();

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Song songs = new Song(songTitle.getText().toString());

                if (songs.toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please Enter A Song Name", Toast.LENGTH_SHORT).show();
                } else {
                    new Firebase(songUrl)
                            .push()
                            .setValue(songs);
                    songTitle.setText("");
                }
            }
        });

        // Use Firebase to populate the list.
        new Firebase(songUrl)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String title = (String) dataSnapshot.child("title").getValue();
                        titleAdapter.add(dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        titleAdapter.remove(dataSnapshot);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        // Delete items when long clicked
        songList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("DELETE");
                alertDialog.setMessage("Are you sure you want delete this?");
                //alertDialog.setIcon(R.drawable.ic_launcher);

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DataSnapshot songAtPostion = (DataSnapshot) songList.getItemAtPosition(position);
                                mSongDeleteUrl=new

                                        Firebase(mFirebaseRootRef+"/data/"+mUserId+"/songs/"+songAtPostion.getKey()

                                );
                                titleAdapter.remove(titleAdapter.getItem(position));
                                titleAdapter.notifyDataSetChanged();
                                mSongDeleteUrl.removeValue();
                            }
                        });

                alertDialog.show();

                return true;
            }
        });

        // Goes to the list activity carrying relevant data for that particular song
        songList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, LyricActivity.class);
                Song song = titleAdapter.getItem(position).getValue(Song.class);
                intent.putExtra(Song.TITLE_KEY, song.getTitle());
                intent.putExtra(Song.UID_KEY, mUserId);
                intent.putExtra(Song.SONG_ID, titleAdapter.getItem(position).getKey());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // When pressing this, the user is logged out of facebook and Firebase.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mFirebaseRootRef.unauth();
            loadLoginView();
            LoginManager.getInstance().logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    // This reloads the login view after the user logs out
    private void loadLoginView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
