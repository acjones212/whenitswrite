package com.austinjones.whenitswrite;

/**
 * Created by austinjones on 3/31/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataSnapshot> {

    // Custom adapter to populate the listView in the MainActivity

    DataSnapshot songInfo;

    public CustomAdapter(Context context, ArrayList<DataSnapshot> dataList ) {
        super(context, R.layout.custom_row, dataList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customView;

        songInfo = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            customView = inflater.inflate(R.layout.custom_row, parent, false);
        } else {
            customView = convertView;
        }

        String songTitle = (String) songInfo.child("title").getValue();
        TextView songTitleView = (TextView) customView.findViewById(R.id.songTitleView);

//        String songId = (String) songInfo.child("songs").getValue();
//        //TextView songIdView = customView.findViewById()
//
//        String songLyrics = (String) songInfo.child("songs").child("lyrics").getValue();
//        TextView songLyricsView = (TextView) customView.findViewById(R.id.lyricsEditText);

//        System.out.println("TITLE "+songTitle);
//        System.out.println("VIEW " + songTitleView);
        songTitleView.setText(songTitle);

        return customView;
    }


}
