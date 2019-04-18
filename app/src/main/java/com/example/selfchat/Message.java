package com.example.selfchat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String message;

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

    /*
    getters
     */
    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
