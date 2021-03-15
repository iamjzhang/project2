package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);

    }

    public void onClick(View view){

        if(loginEmail.getText().toString().trim().isEmpty()){
            loginEmail.setBackgroundColor(Color.RED);

            return;
        }

        else if(loginPassword.getText().toString().trim().isEmpty()) {
            loginPassword.setBackgroundColor(Color.RED);

            return;
        }
        else {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
    }
}