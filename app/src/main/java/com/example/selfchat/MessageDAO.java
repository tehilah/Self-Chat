package com.example.selfchat;

import android.arch.lifecycle.LiveData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MessageDAO {

    @Query("SELECT * FROM Messages")
    LiveData<List<Message>> getAllMessages();

    @Query("SELECT message FROM messages WHERE id LIKE :i")
    LiveData<Message> findById(int i);

    @Query("SELECT message FROM messages WHERE message LIKE :m")
    LiveData<Message> findByString(String m);

    @Insert
    void insert(Message m);

    @Delete
    void delete(Message m);
}
