package com.example.remindme;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name="title")
    String title;
    @ColumnInfo(name="details")
    String details;
    @ColumnInfo(name="date")
    String date;
    @ColumnInfo(name="time")
    String time;
    @ColumnInfo(name="isDone")
    Boolean isDone;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
