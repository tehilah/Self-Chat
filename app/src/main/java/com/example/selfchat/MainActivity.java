package com.example.selfchat;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /*
    constants
    */
    public static final String EMPTY_MESSAGE = "";
    public static final String ERROR_MESSAGE = "Woops! you can't send an empty message";
    public static final String MESSAGES = "messages";
    public static final String LIST_STATE = "listState";
    public static final String EDIT_TEXT = "editText";

    /*
    variables
     */
    private EditText chatText;
    private ArrayList<String> messages = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button send = findViewById(R.id.btn);
        chatText = findViewById(R.id.editText);

        initRecyclerView();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String message = chatText.getText().toString();
                if (message.equals(EMPTY_MESSAGE)){
                    int duration = Toast.LENGTH_LONG;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, ERROR_MESSAGE, duration);
                    toast.show();
                }
                else{
                    adapter.addMessage(message);
                    adapter.notifyDataSetChanged();
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
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(messages);
        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Parcelable listState = linearLayoutManager.onSaveInstanceState();
        outState.putStringArrayList(MESSAGES,adapter.getMessageList());
        outState.putParcelable(LIST_STATE, listState);
        outState.putString(EDIT_TEXT, chatText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(LIST_STATE);
            linearLayoutManager.onRestoreInstanceState(listState);
            adapter.setMessageList(savedInstanceState.getStringArrayList(MESSAGES));
            chatText.setText(savedInstanceState.get(EDIT_TEXT).toString());
        }
    }
}
