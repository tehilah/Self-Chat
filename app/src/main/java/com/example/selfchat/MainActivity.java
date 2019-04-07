package com.example.selfchat;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    /*
    constants
    */
    private static final String TAG1 = "Save state";
    private static final String TAG2 = "Restore state";
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
    private RecyclerView recyclerView;

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
                Log.d(TAG1, "onClick: clicked.");
                String message = chatText.getText().toString();
                if (message.equals(EMPTY_MESSAGE)){
                    int duration = Toast.LENGTH_LONG;
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context, ERROR_MESSAGE, duration);
                    toast.show();
                }
                else{
                    adapter.addMessage(message);
//                messages.add(chatText.getText().toString());
                    chatText.setText("");
                    chatText.requestFocus();
                }

            }
        });
    }

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(this, messages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        Log.d(TAG1, "onRotate: rotated.");
        super.onSaveInstanceState(outState);
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putStringArrayList(MESSAGES,adapter.getMessageList());
        outState.putParcelable(LIST_STATE, listState);
        outState.putString(EDIT_TEXT, chatText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.d(TAG2, "onRotate: rotated.");
        super.onSaveInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(LIST_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
            adapter.setMessageList(savedInstanceState.getStringArrayList(MESSAGES));
            chatText.setText(savedInstanceState.get(EDIT_TEXT).toString());
        }
    }
}
