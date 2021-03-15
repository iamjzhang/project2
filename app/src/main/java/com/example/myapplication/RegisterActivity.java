package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.text.TextUtils;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName, lastName, emailAddress, birthday, password, password2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        emailAddress = (EditText)findViewById(R.id.emailAddress);
        birthday = (EditText)findViewById(R.id.birthday);
        password = (EditText)findViewById(R.id.password);
        password2 = (EditText)findViewById(R.id.confirmPassword);

    }

    public void onClick(View view){


        if(firstName.getText().toString().trim().isEmpty() || firstName.getText().toString().length() < 3 || firstName.getText().toString().length() > 30){
            firstName.setBackgroundColor(Color.RED);

            return;
        }
        else if(lastName.getText().toString().trim().isEmpty() || lastName.getText().toString().length() < 3 || lastName.getText().toString().length() > 30){
            lastName.setBackgroundColor(Color.RED);

            return;
        }
        else if(emailAddress.getText().toString().trim().isEmpty() || emailAddress.getText().toString().length() < 3 || emailAddress.getText().toString().length() > 30){
            emailAddress.setBackgroundColor(Color.RED);

            return;
        }
        else if(!password.getText().toString().trim().equals(password2.getText().toString().trim()) ){
            password.setBackgroundColor(Color.RED);
            password2.setBackgroundColor(Color.RED);
            Toast.makeText(RegisterActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }

    }
}