package com.example.selfchat;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Messages")
public class Message {

    @PrimaryKey
    private int id;

    private String message;

    public Message(String m){
        message = m;
    }

    /*
   setters
    */
    public void setId(int id) {
        this.id = id;
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
