package com.timecat.module.main.miniapp.models;

public class AudioFileModel {
    private String album;
    private String albumId;
    private String artist;
    private String duration;
    private String name;
    private String path;
    private String title;
    private int totalDuration;

    public AudioFileModel(){}

    public AudioFileModel(String path, String title, String album, String artist, String name, String duration, String albumId) {
        this.path = path;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.name = name;
        this.duration = duration;
        this.albumId = albumId;
    }

    public int getTotalDuration() {
        return this.totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }
}
