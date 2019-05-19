package com.example.selfchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.android.device.DeviceName;

public class MessageDetailsActivity extends AppCompatActivity {
    private Intent intent;
    private TextView textViewTimestamp;
    private TextView textViewDeviceName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_message_details);
        intent = getIntent();
        initializePage();
    }

    private void initializePage() {
        textViewTimestamp = findViewById(R.id.timestamp_result);
        textViewDeviceName = findViewById(R.id.device_result);

        String timestamp = intent.getStringExtra("timestamp");
        textViewTimestamp.setText(timestamp);

        DeviceName.with(this).request(new DeviceName.Callback() {
            @Override
            public void onFinished(DeviceName.DeviceInfo info, Exception error) {
                String deviceName = info.marketName;
                textViewDeviceName.setText(deviceName);
            }
        });
    }

    public void deleteMessage(View v) {
        String docRefPath = intent.getStringExtra("document reference path");
        DocumentReference messageRef = db.document(docRefPath);
        new DeleteMessageAsyncTask().execute(messageRef);
        startActivity(new Intent(MessageDetailsActivity.this, MainActivity.class));
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
