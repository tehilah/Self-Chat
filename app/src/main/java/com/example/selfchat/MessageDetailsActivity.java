package com.example.selfchat;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessageDetailsActivity extends AppCompatActivity {
    private Intent intent;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_message_details);
        intent = getIntent();
        initializePage();
    }

    private void initializePage() {
        TextView textViewTimestamp = findViewById(R.id.timestamp_result);
        TextView textViewDeviceName = findViewById(R.id.device_result);

        textViewTimestamp.setText(intent.getStringExtra("timestamp"));
        textViewDeviceName.setText(intent.getStringExtra("phone sender"));
    }

    public void deleteMessage(View v) {
        String docRefPath = intent.getStringExtra("document reference path");
        DocumentReference messageRef = db.document(docRefPath);
        new DeleteMessageAsyncTask().execute(messageRef);
        Intent i = new Intent(MessageDetailsActivity.this, MainActivity.class);
        i.putExtra("IsFirstRun", false);
        startActivity(i);
        finish();
    }

    /**
     * Asynchronous class for deleting messages
     */
    private static class DeleteMessageAsyncTask extends AsyncTask<DocumentReference, Void, Void> {
        @Override
        protected Void doInBackground(DocumentReference... references) {
            references[0].delete();
            return null;
        }
    }

}
