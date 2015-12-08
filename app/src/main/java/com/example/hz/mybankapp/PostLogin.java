package com.example.hz.mybankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class PostLogin extends AppCompatActivity {
    String email;
    String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);

        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        
        String[] arrayInB = extras.getStringArray("my_array");
        email = arrayInB[0];
        pass = arrayInB[1];

    }
    public void changePassClick(View view){
        if(view.getId() == R.id.changeEmail){

        }
    }
    public void changeEmClick(View view){
        if(view.getId() == R.id.changeEmail){

        }
    }
    public void chatClick(View view){
        if(view.getId() == R.id.chat){

        }
    }

}
