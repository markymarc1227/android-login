package com.example.finalproject;

import android.content.Context;
import android.widget.Toast;

public class ToastNotif {
    public static void message(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
