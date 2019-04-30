package com.example.selfchat;

import androidx.lifecycle.LiveData;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MessageDAO {

    @Query("SELECT * FROM Messages")
    LiveData<List<Message>> getAllMessages();

    @Insert
    void insert(Message m);

    @Delete
    void delete(Message m);
}
