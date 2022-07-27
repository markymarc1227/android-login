package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class AdminPage extends AppCompatActivity {
    EditText name, pass, updateold, updatenew, newpass, delete;
    DbAdapter helper;
    RadioButton admin_btn;
    String usertype = null;
    Button admin_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        name = (EditText) findViewById(R.id.editName);
        pass = (EditText) findViewById(R.id.editPass);
        delete = (EditText) findViewById(R.id.deleteData);
        updateold = (EditText) findViewById(R.id.currentName);
        updatenew = (EditText) findViewById(R.id.newName);
        newpass = (EditText) findViewById(R.id.changePass);
        admin_btn = (RadioButton) findViewById(R.id.rad_admin);
        admin_out = (Button) findViewById(R.id.sign_out);

        helper = new DbAdapter(this);

        if(UserInfo.privilege.equals("Admin")){
            admin_btn.setEnabled(false);
        }

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rad_admin:
                if (checked)
                usertype = "Admin";
                break;
            case R.id.rad_user:
                if (checked)
                    usertype = "User";
                break;
            default:
                usertype = null;
                break;
        }
    }

    public void addUser(View view)
    {
        String t1 = name.getText().toString();
        String t2 = pass.getText().toString();
        String t3 = usertype;
        if(t1.isEmpty() || t2.isEmpty())
        {
            ToastNotif.message(getApplicationContext(),"Enter Both Name and Password");
        }
        else if(usertype == null)
        {
            ToastNotif.message(getApplicationContext(),"Select a user type");
        }
        else
        {
            long id = helper.insertData(t1,t2,t3);
            if(id<=0)
            {
                ToastNotif.message(getApplicationContext(),"Insertion Unsuccessful");
                name.setText("");
                pass.setText("");
            } else
            {
                ToastNotif.message(getApplicationContext(),"Insertion Successful");
                name.setText("");
                pass.setText("");
            }
        }
    }

    public void viewdata(View view)
    {
        String data = helper.getData();
        ToastNotif.message(this,data);
    }

    public void delete( View view)
    {
        String uname = delete.getText().toString();
        String user_privilege = helper.getUserPrivilege(uname);
        if(uname.isEmpty())
        {
            ToastNotif.message(getApplicationContext(),"Enter Data");
        }
        else if(user_privilege.equals("Admin") && UserInfo.privilege.equals("Admin")){
            ToastNotif.message(getApplicationContext(),"Only SuperAdmin can delete Admins.");
        }
        else{

            int a= helper.delete(uname);
            if(a<=0)
            {
                ToastNotif.message(getApplicationContext(),"Unsuccessful");
                delete.setText("");
            }
            else
            {
                ToastNotif.message(this, "DELETED");
                delete.setText("");
            }
        }
    }

    public void update( View view)
    {
        String u1 = updateold.getText().toString();
        String u2 = updatenew.getText().toString();
        String u3 = newpass.getText().toString();
        String user_privilege = helper.getUserPrivilege(u1);
        if(u1.isEmpty() || u2.isEmpty() || u3.isEmpty())
        {
            ToastNotif.message(getApplicationContext(),"Enter Complete Data");
        }
        else if(user_privilege.equals("Admin") && UserInfo.privilege.equals("Admin")){
            ToastNotif.message(getApplicationContext(),"Only SuperAdmin can update Admins.");
        }
        else
        {
            int a= helper.updateName( u1, u2, u3);
            if(a<=0)
            {
                ToastNotif.message(getApplicationContext(),"Unsuccessful");
                updateold.setText("");
                updatenew.setText("");
                newpass.setText("");
            } else {
                ToastNotif.message(getApplicationContext(),"Updated");
                updateold.setText("");
                updatenew.setText("");
                newpass.setText("");
            }
        }
    }
    public void admin_signout(View view) {
        UserInfo.username="";
        UserInfo.password="";
        UserInfo.privilege="";
        Intent in = new Intent(view.getContext(), MainActivity.class);
        startActivity(in);
        finish();
    }
}