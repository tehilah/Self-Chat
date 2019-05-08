package com.example.selfchat;


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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
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

//        mAdapter.setOnItemClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                final RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//                alertDialogBuilder
//                        .setMessage(CONFIRM_DELETE)
//                        .setCancelable(false)
//                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                mAdapter.deleteItem(viewHolder.getAdapterPosition());
//
//                            }
//                        }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//                return true;
//            }
//        });
//    }

        mAdapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder
                        .setMessage(CONFIRM_DELETE)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, delete message
                                mAdapter.deleteItem(position);
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
     * Asynchronous class for adding message to db
     */
    private static class InsertMessageAsyncTask extends AsyncTask<String, Void, Void> {
        private WeakReference<MainActivity> activityWeakReference;

        private InsertMessageAsyncTask(MainActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }
        @Override
        protected Void doInBackground(final String... messages) {
            MainActivity activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing()){
                return null;
            }
            
            // get timestamp
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-YYYY, hh:mm:ss");
            s.setTimeZone(TimeZone.getTimeZone("GMT+3"));
            final String timestamp = s.format(new Date());

            // find the next free id and add message to db
            activity.messageRef.orderBy("id", Query.Direction.DESCENDING).limit(1).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Message m = documentSnapshot.toObject(Message.class);
                                Message newMessage = new Message(messages[0], timestamp);
                                newMessage.setId(m.getId()+1);
                                FirebaseFirestore.getInstance().collection("Messages").add(newMessage);
                            }
                        }
                    });

            activity.chatText.setText(EMPTY_MESSAGE);
            return null;
        }
    }


}
