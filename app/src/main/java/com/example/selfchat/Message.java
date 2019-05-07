package com.example.selfchat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String message;

    private String timestamp = "";

    public Message(){}

    public Message(String m){
        message = m;
    }

    /*
   setters
    */
    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /*
    getters
     */
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
