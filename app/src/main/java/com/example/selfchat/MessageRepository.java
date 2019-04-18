package com.example.selfchat;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class MessageRepository {
    private MessageDAO messageDAO;
    private LiveData<List<Message>> allMessages;

    public MessageRepository(Application app){
        AppDatabase database = AppDatabase.getInstance(app);
        messageDAO = database.messageDAO();
        allMessages = messageDAO.getAllMessages();
    }

    public void insert(Message message){
        new InsertMessageAsyncTask(messageDAO).execute(message);
    }

    public void delete(Message message){
        new DeleteMessageAsyncTask(messageDAO).execute(message);
    }

    public LiveData<List<Message>> getAllMessages(){
        return allMessages;
    }

    /*
    Async Classes (because Room can't run on main thread)
     */
    private static class InsertMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDAO messageDAO;

        private InsertMessageAsyncTask(MessageDAO messageDAO){
            this.messageDAO = messageDAO;
        }
        @Override
        protected Void doInBackground(Message... messages) {
            messageDAO.insert(messages[0]);
            return null;
        }
    }

    private static class DeleteMessageAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDAO messageDAO;

        private DeleteMessageAsyncTask(MessageDAO messageDAO){
            this.messageDAO = messageDAO;
        }
        @Override
        protected Void doInBackground(Message... messages) {
            messageDAO.delete(messages[0]);
            return null;
        }
    }
}
