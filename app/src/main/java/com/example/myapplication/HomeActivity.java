package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;

    //set buttons for two instances, login and register
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginEmail = (EditText)findViewById(R.id.loginEmail);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void login(){
        if(loginEmail.getText().toString().trim().isEmpty() || loginEmail.getText().toString().length() < 3 || loginEmail.getText().toString().length() > 30){
            loginEmail.setBackgroundColor(Color.RED);

            return;
        }
        else if(loginPassword.getText().toString().trim().isEmpty() || loginPassword.getText().toString().length() < 3 || loginPassword.getText().toString().length() > 30) {
            loginPassword.setBackgroundColor(Color.RED);

            return;
        }
        else {
            Intent i = new Intent(HomeActivity.this, RulesActivity.class);
            startActivity(i);
        }
    }

    private void register(){
        Intent i = new Intent(HomeActivity.this, RegisterActivity.class);
        startActivity(i);
    }

}