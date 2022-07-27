package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class AdminPage extends AppCompatActivity {
    AlertDialog.Builder builder;

    EditText name, pass, updateold, updatenew, newpass, delete;
    TextView displaydata;
    DbAdapter helper;
    RadioButton admin_btn;
    String usertype = null;
    Button admin_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        builder = new AlertDialog.Builder(this);

        name = (EditText) findViewById(R.id.editName);
        pass = (EditText) findViewById(R.id.editPass);
        delete = (EditText) findViewById(R.id.deleteData);
        updateold = (EditText) findViewById(R.id.currentName);
        updatenew = (EditText) findViewById(R.id.newName);
        newpass = (EditText) findViewById(R.id.changePass);
        admin_btn = (RadioButton) findViewById(R.id.rad_admin);
        admin_out = (Button) findViewById(R.id.sign_out);
        displaydata = (TextView) findViewById(R.id.displaydata);

        helper = new DbAdapter(this);

        if(UserInfo.privilege.equals("Admin")){
            admin_btn.setEnabled(false);
            admin_btn.setTextColor(Color.parseColor("#4f4f4f"));
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
        String add_name = name.getText().toString();
        String add_pass = pass.getText().toString();
        String add_usertype = usertype;
        if(add_name.isEmpty() || add_pass.isEmpty())
        {
            ToastNotif.message(getApplicationContext(),"Enter Both Name and Password");
        }
        else if(add_usertype == null)
        {
            ToastNotif.message(getApplicationContext(),"Select a user type");
        }
        else
        {
            long id = helper.insertData(add_name,add_pass,add_usertype);
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
        String message = "                   User List\nID  Type  " +
                "Username  Password \n" + data;
//        ToastNotif.message(this,message);
        showDialog(message);
        displaydata.setText(message);
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
            showDialog("Only SuperAdmin can delete Admins.");
        }
        else{

            int a= helper.delete(uname);
            if(a<=0)
            {
                ToastNotif.message(getApplicationContext(),"UNSUCCESSFUL");
                delete.setText("");
            }
            else
            {
                ToastNotif.message(this, "SUCCESSFULLY DELETED");
                delete.setText("");
            }
        }
    }

    public void update( View view)
    {
        String up_oldname = updateold.getText().toString();
        String up_newname = updatenew.getText().toString();
        String up_newpass = newpass.getText().toString();
        String user_privilege = helper.getUserPrivilege(up_oldname);
        if(up_oldname.isEmpty() || up_newname.isEmpty() || up_newpass.isEmpty())
        {
            ToastNotif.message(getApplicationContext(),"Enter Complete Data");
        }
        else if(user_privilege.equals("Admin") && UserInfo.privilege.equals("Admin")){
            showDialog("Only SuperAdmin can update Admins.");
        }
        else
        {
            int a= helper.updateName( up_oldname, up_newname, up_newpass);
            if(a<=0)
            {
                ToastNotif.message(getApplicationContext(),"UNSUCCESSFUL");
                updateold.setText("");
                updatenew.setText("");
                newpass.setText("");
            } else {
                ToastNotif.message(getApplicationContext(),"FAILED TO UPDATE");
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

    private void showDialog(String message)
    {
        builder.setTitle("ALERT")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}