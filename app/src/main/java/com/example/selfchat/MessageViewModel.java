package com.example.selfchat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class MessageViewModel extends AndroidViewModel {
    private MessageRepository repository;
    private LiveData<List<Message>> allMessages;

    public MessageViewModel(@NonNull Application application) {
        super(application);
        repository = new MessageRepository(application);
        allMessages = repository.getAllMessages();
    }

    public void insert(Message message){
        repository.insert(message);
    }

    public void delete(Message message){
        repository.delete(message);
    }

    public LiveData<List<Message>> getAllMessages(){
        return allMessages;
    }
}
