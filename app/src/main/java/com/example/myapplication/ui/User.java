package com.example.myapplication.ui;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private final String username;
    private final String password;
    private final String website;

    private final String url;
    public boolean isHeaderVisible;
    private final long timestamp;

    public User(String username, String password, String website,String url,boolean isHeaderVisible, long timestamp) {
        this.username = username;
        this.password = password;
        this.website = website;
        this.url = url;
        this.isHeaderVisible = isHeaderVisible;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getWebsite() { return website; }

    public String getUrl() { return url; }
    public boolean isHeaderVisible() {return isHeaderVisible;}

    public long getTimestamp() { return timestamp; }



}