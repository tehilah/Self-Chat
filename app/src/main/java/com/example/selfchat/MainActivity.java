package com.example.selfchat;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button send = findViewById(R.id.btn);

        chatText = findViewById(R.id.editText);

        initRecyclerView();

        final MessageViewModel messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        messageViewModel.getAllMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable List<Message> messages) {
                //todo: see what happens if i erase the setmessage
                Log.d(LIST_SIZE,
                        String.valueOf(messageViewModel.getAllMessages().getValue().size()));
                adapter.setMessageList(messages);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("onClick", "clicked");
                String message = chatText.getText().toString();
                if (message.equals(EMPTY_MESSAGE)){
                    int duration = Toast.LENGTH_LONG;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, ERROR_MESSAGE, duration);
                    toast.show();
                }
                else{
                    adapter.addMessage(message);
                    messageViewModel.insert(new Message(message));
                    chatText.setText(EMPTY_MESSAGE);
                    chatText.requestFocus();
                }

            }
        });
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("OnClick", "message was clicked");
                final int pos = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage(CONFIRM_DELETE)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, delete message
                                messageViewModel.delete(adapter.getMessageAt(pos));

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
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}