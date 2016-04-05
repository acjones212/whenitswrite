package com.austinjones.whenitswrite;

/**
 * Created by austinjones on 3/31/16.
 */
public class Song {

    public static final String TITLE_KEY = "songs";
    public static final String UID_KEY = "uid";
    public static final String SONG_ID = "songid";

    private String title;
    private boolean delete;
    private String id;
    private String lyrics;

    public Song() {}

    public Song(String title) {
        this.title = title;
        this.delete = delete;
        this.id = id;
        this.lyrics = lyrics;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString(){
        return title;
    }

    public boolean isDelete(){
        return delete;
    }

    public void setDelete(boolean delete){
        this.delete = delete;
    }

    public String getId(String id) {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}