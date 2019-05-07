package com.example.selfchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


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
    private List<Message> messages = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private boolean appRunning = false;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        
        Button send = findViewById(R.id.btn);

        chatText = findViewById(R.id.editText);

        initRecyclerView();

//        final MessageViewModel messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
//        messageViewModel.getAllMessages().observe(this, new Observer<List<Message>>() {
//            @Override
//            public void onChanged(@Nullable List<Message> messages) {
//                if (!appRunning) {
//                    Log.d(LIST_SIZE,
//                            String.valueOf(messageViewModel.getAllMessages().getValue().size()));
//                    appRunning = true;
//                }
//                adapter.setMessageList(messages);
//            }
//        });

        // click listener for editText
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String message = chatText.getText().toString();
                Map<String, String> messageMap = new HashMap<>();

                if (message.equals(EMPTY_MESSAGE)){
                    int duration = Toast.LENGTH_LONG;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, ERROR_MESSAGE, duration);
                    toast.show();
                }
                else{
                    Message m = new Message(message);
                    messageMap.put("message", message);
                    SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
                    s.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                    String format = s.format(new Date());
                    m.setTimestamp(format);

                    Log.d("timestamp", format);

                    db.collection("Messages").add(m);
                    adapter.addMessage(message);
//                    messageViewModel.insert(new Message(message));
                    chatText.setText(EMPTY_MESSAGE);
                }

            }
        });


        // long click listener for textViews
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final int pos = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage(CONFIRM_DELETE)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, delete message
//                                messageViewModel.delete(adapter.getMessageAt(pos));

                            }
                        }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close the dialog box and do nothing
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    /**
     * Initializes the RecyclerView
     */
    private void initRecyclerView(){
        Log.d("onInit", "init");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(messages);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}