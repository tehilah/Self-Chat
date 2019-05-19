package com.example.selfchat;


import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    /*
    constants
    */
    public static final String EMPTY_MESSAGE = "";
    public static final String ERROR_MESSAGE = "Woops! you can't send an empty message";
    public static final String DOCUMENT_REF_PATH = "document reference path";
    public static final String TIMESTAMP = "timestamp";

    /*
    variables
     */
    private EditText chatText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MessageAdapter mAdapter;
    private CollectionReference messageRef = db.collection("Messages");
    private ExecutorService executor = Executors.newCachedThreadPool();
    private TextView username;
    private Intent i;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = getIntent();
        username = (TextView) findViewById(R.id.user_name);
        chatText = findViewById(R.id.editText);
        initToolbar();
        checkIfLoggedIn();
        initRecyclerView();
        ListenForDelete();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarText = toolbar.findViewById(R.id.toolbarText);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void updateUI(final TextView textView, final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(message);
            }
        });
    }
    /**
     * check if the user has instantiated his name, if so stay on main screen. if not navigate
     * to login page
     */
    private void checkIfLoggedIn() {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    db.collection("Default").document("User").get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (getIntent().getBooleanExtra("IsFirstRun", true)) {
                                if (documentSnapshot.get("name") == null) {
                                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
                                } else {
                                    String name = documentSnapshot.get("name").toString();
                                    i.putExtra("name", name);
                                    updateUI(toolbarText, "Hello " + i.getStringExtra("name") + "!");
                                }
                            }
                            else{
                                if (i.getStringExtra("name") != null) {
                                    updateUI(toolbarText, "Hello " + i.getStringExtra("name") + "!");
                                }
                            }
                        }
                    });
                }
            });
        }

    private void addMessageToFireStore(final String message){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // get timestamp
                SimpleDateFormat s = new SimpleDateFormat("dd-MM-YYYY, hh:mm:ss", Locale.getDefault());
                s.setTimeZone(TimeZone.getTimeZone("GMT+3"));
                final String timestamp = s.format(new Date());

                // find the next free id and add message to db
                messageRef.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if(queryDocumentSnapshots.isEmpty()){
                                    Message newMessage = new Message(message, timestamp);
                                    FirebaseFirestore.getInstance().collection("Messages").add(newMessage);
                                }
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    Message m = documentSnapshot.toObject(Message.class);
                                    Message newMessage = new Message(message, timestamp);
                                    newMessage.setId(m.getId()+1);
                                    messageRef.add(newMessage);
                                }
                            }
                        });

                updateUI(chatText, EMPTY_MESSAGE);
            }
        });
    }

    private void ListenForDelete() {
        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot ds, int position) {
                Message m = ds.toObject(Message.class);
                if(m != null){
                    Intent intent = new Intent(MainActivity.this, MessageDetailsActivity.class);
                    intent.putExtra(TIMESTAMP, m.getTimestamp());
                    intent.putExtra(DOCUMENT_REF_PATH, ds.getReference().getPath());
                    startActivity(intent);
                }

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
        else{ addMessageToFireStore(message); }
    }
    /**
     * Initializes the RecyclerView
     */
    private void initRecyclerView(){
        Query q = messageRef.orderBy("id", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(q, Message.class)
                .build();
        mAdapter = new MessageAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
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

}
