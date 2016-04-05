package com.austinjones.whenitswrite;

/**
 * Created by austinjones on 3/31/16.
 */

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class LyricActivity extends AppCompatActivity {

    private Firebase mFirebaseRootRef;
    private MediaRecorder media;
    private String outputFile = null;
    private Button startRecord,stop,play;
    private ImageView back;
    private EditText lyricText;
    private TextView songTitle;
    private Firebase lyricsUrl;
    private String mUserId;
    private String songId;
    private String titleText;
    ArrayList<DataSnapshot> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        titleText = getIntent().getStringExtra(Song.TITLE_KEY);
        mUserId = getIntent().getStringExtra(Song.UID_KEY);
        songId = getIntent().getStringExtra(Song.SONG_ID);

        mFirebaseRootRef = new Firebase("https://whenitswrite.firebaseio.com");
        Log.d("Lyics",songId);
        lyricsUrl = mFirebaseRootRef.child("/data/" + mUserId + "/songs/" + songId + "/lyrics");

        lyricText = (EditText) findViewById(R.id.lyricsEditText);
        songTitle = (TextView) findViewById(R.id.songTitle);
//        startRecord = (Button)findViewById(R.id.recordBtn);
//        stop = (Button)findViewById(R.id.stopBtn);
//        play = (Button)findViewById(R.id.playBtn);
        back = (ImageView)findViewById(R.id.backBtn);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if(b != null){
            String name = (String) b.get(titleText);
            songTitle.setText(name);
        }

        // Sets a song's titleText to the songTitle textview
        songTitle.setText(titleText);

        // Sends the user back to the MainActivity when pressed
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    // Sets the text in the lyricText edittext field to a string and saves it under a
    // Firebase reference
    @Override
    protected void onPause() {
        super.onPause();
        String lyrics = lyricText.getText().toString();
        lyricsUrl.setValue(lyrics);
    }

    // Pull the lyrics from a song, if it has any, and puts it into the lyricText edittext field
    @Override
    protected void onResume() {
        super.onResume();
        lyricsUrl.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lyricText.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}

//        stop.setEnabled(false);
//        play.setEnabled(false);
//        outputFile = Environment.getExternalStorageDirectory().
//                getAbsolutePath() + "/recording.3gp";
//
//        media = new MediaRecorder();
//        media.setAudioSource(MediaRecorder.AudioSource.MIC);
//        media.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        media.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        media.setOutputFile(outputFile);


        // Send the user back to the MainActivity when pressed


//    public void startRecord(View view){
//        try {
//            media.prepare();
//            media.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        startRecord.setEnabled(false);
//        stop.setEnabled(true);
//        Toast.makeText(getApplicationContext(), "Recording Your Masterpiece!", Toast.LENGTH_SHORT).show();
//
//    }
//
//    public void stop(View view){
//        media.stop();
//        media.release();
//        media = null;
//        stop.setEnabled(false);
//        play.setEnabled(true);
//        Toast.makeText(getApplicationContext(), "Stopping So Soon?",
//                Toast.LENGTH_SHORT).show();
//    }
//
//    public void play(View view) throws IllegalArgumentException,
//            SecurityException, IllegalStateException, IOException{
//
//        MediaPlayer m = new MediaPlayer();
//        m.setDataSource(outputFile);
//        m.prepare();
//        m.start();
//        Toast.makeText(getApplicationContext(), "Playing Your Next Hit", Toast.LENGTH_LONG).show();
//
//    }


