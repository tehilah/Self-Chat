package com.example.selfchat;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

    /*
    variables
     */
    private EditText chatText;
    private List<Message> messages = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private int counter;

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
                Log.d("onChanged", "changed");
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
                    chatText.setText("");
                    chatText.requestFocus();
                }

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
