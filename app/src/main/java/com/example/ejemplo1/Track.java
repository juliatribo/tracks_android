package com.example.ejemplo1;

public class Track {
    private String id;
    private String singer;
    private String title;

    public Track(){

    }

    public Track(String title, String singer, String id) {
        this.id = id;
        this.singer = singer;
        this.title = title;
    }

    public Track(String singer, String title) {
        this.singer = singer;
        this.title = title;
    }

    @Override
    public String toString() {
        return this.singer + " " + this.title;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
