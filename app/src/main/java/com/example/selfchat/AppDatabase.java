package com.example.selfchat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Message.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract MessageDAO messageDAO();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "message_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}

