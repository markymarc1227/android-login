package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    DbAdapter helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.user_edit);
        password = (EditText) findViewById(R.id.pass_edit);
        login = (Button) findViewById(R.id.login_btn);
        helper = new DbAdapter(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                Boolean userExists = helper.fetchUser(user, pass);
                if (user.equals("admin") && pass.equals("admin")){
                    UserInfo.username = user;
                    UserInfo.password = pass;
                    UserInfo.privilege = "superadmin";
                    Intent in = new Intent(view.getContext(), AdminPage.class);
                    startActivity(in);
                    finish();
                } else if (userExists){
                    UserInfo.username = user;
                    UserInfo.password = pass;
                    UserInfo.privilege = helper.checkPrivilege(user, pass);
                    if (UserInfo.privilege.equals("Admin") ){
                        Intent in = new Intent(view.getContext(), AdminPage.class);
                        startActivity(in);
                        finish();
                    }
                    else if (UserInfo.privilege.equals("User")){
                        Intent in = new Intent(view.getContext(), UserPage.class);
                        startActivity(in);
                        finish();
                    }
                    else {
                        ToastNotif.message(getApplicationContext(),"User was found!");
                    }
                }
                else {
                    ToastNotif.message(getApplicationContext(),"Invalid Credentials");
                }
                username.setText("");
                password.setText("");
            }
        });

    }





}