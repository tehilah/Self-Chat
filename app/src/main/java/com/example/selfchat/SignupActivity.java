package com.example.selfchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private Button setNameButton;
    private EditText editText;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.putExtra("IsFirstRun", false);
        setNameButton = (Button)findViewById(R.id.btnSignup);
        editText = (EditText) findViewById(R.id.editText);
        checkForEnteredName();
        configureSkip();
        configureName();

    }

    /**
     * When user clicks "skip" he will be directed to the main screen
     */
    private void configureSkip() {
        Button skipButton = (Button)findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Listen to see if user enters name to editText, if so, show the submit name button
     */
    private void checkForEnteredName(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setNameButton.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * If user submits name save it in fire-store
     */
    private void configureName() {
        setNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                AddToDatabase task = new AddToDatabase();
                task.execute(name);
                startActivity(intent);
                finish();
            }
        });
    }

    private static class AddToDatabase extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Map<String, String> userName = new HashMap<>();
            userName.put("name", strings[0]);
            FirebaseFirestore.getInstance().collection("Default")
                    .document("User").set(userName);
            return null;
        }
    }
}



