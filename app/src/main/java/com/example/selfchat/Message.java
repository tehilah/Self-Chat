package com.example.selfchat;


public class Message {

    /*
    variables
    */
    private int id;
    private String message;
    private String timestamp;

    public Message(){}

    public Message(String m, String ts){
        message = m;
        timestamp = ts;
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
