package com.example.musicapp;

public class Music {
    private String title;
    private String name;
    private String path;
    public Music(){}

    public Music(String title, String name, String path ) {
        this.title=title;
        this.name = name;
        this.path=path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
