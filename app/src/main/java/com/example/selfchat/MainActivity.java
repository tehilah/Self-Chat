package com.example.selfchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {
    /*
    constants
    */
    public static final String EMPTY_MESSAGE = "";
    public static final String ERROR_MESSAGE = "Woops! you can't send an empty message";
    public static final String LIST_SIZE = "ListSize";
    public static final String CONFIRM_DELETE = "Are you sure you want to delete?";
    final Context context = this;

    /*
    variables
     */
    private EditText chatText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MessageAdapter mAdapter;
    private CollectionReference messageRef = db.collection("Messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();
        chatText = findViewById(R.id.editText);

        // long click listener for textViews
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage(CONFIRM_DELETE)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAdapter.deleteItem(position);
                            }
                        }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * Function that handles sending the message after the button was clicked
     * @param v: view
     */
    public void sendMessage(View v){
        String message = chatText.getText().toString();
        if (message.trim().isEmpty()){ toastEmptyMessage(); }
        else{
            InsertMessageAsyncTask task = new InsertMessageAsyncTask(this);
            task.execute(message);
        }
    }
    /**
     * Initializes the RecyclerView
     */
    private void initRecyclerView(){
        Query q = messageRef.orderBy("timestamp", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(q, Message.class)
                .build();
        mAdapter = new MessageAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        Log.d("onStart", "onStart: start listening");
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void toastEmptyMessage(){
        int duration = Toast.LENGTH_LONG;
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, ERROR_MESSAGE, duration);
        toast.show();
    }

    /**
     * Asynchronous class for sending messages
     */
    private static class InsertMessageAsyncTask extends AsyncTask<String, Void, Void> {
        private WeakReference<MainActivity> activityWeakReference;

        private InsertMessageAsyncTask(MainActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }
        @Override
        protected Void doInBackground(String... messages) {
            MainActivity activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing()){
                return null;
            }
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-YYYY, hh:mm:ss");
            s.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            String timestamp = s.format(new Date());
            Message m = new Message(messages[0], timestamp);
            activity.messageRef.add(m);
            activity.chatText.setText(EMPTY_MESSAGE);
            return null;
        }
    }
}
