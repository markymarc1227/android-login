package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class UserPage extends AppCompatActivity {
    TextView user_name;
    Button signout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        user_name = (TextView) findViewById(R.id.user_name);
        signout_btn = (Button) findViewById(R.id.usersign_out);

        user_name.setText(UserInfo.username);
    }

    public void signOut(View view) {
        UserInfo.username="";
        UserInfo.password="";
        UserInfo.privilege="";
        Intent in = new Intent(view.getContext(), MainActivity.class);
        startActivity(in);
        finish();
    }
}