package com.example.selfchat;


public class Message {

    /*
    variables
    */
    private int id;
    private String message;
    private String timestamp;
    private String phoneSender;

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

    public void setTimestamp(String t){
        timestamp = t;
    }

    public void setPhoneSender(String phone){
        phoneSender = phone;
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

    public String getPhoneSender() {
        return phoneSender;
    }

}
