package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbAdapter {
    DbHelper myhelper;
    public DbAdapter(Context context)
    {
        myhelper = new DbHelper(context);
    }

    public long insertData(String name, String pass, String privilege)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.PRIVILEGE, privilege);
        contentValues.put(DbHelper.NAME, name);
        contentValues.put(DbHelper.PASSWORD, pass);
        long id = dbb.insert(DbHelper.TABLE_NAME, null , contentValues);
        return id;
    }

    public String getData()  {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {DbHelper.UID, DbHelper.PRIVILEGE, DbHelper.NAME,DbHelper.PASSWORD};
        Cursor cursor =db.query(DbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())   {
            @SuppressLint("Range") int cid =cursor.getInt(cursor.getColumnIndex(DbHelper.UID));
            @SuppressLint("Range") String privilege =cursor.getString(cursor.getColumnIndex(DbHelper.PRIVILEGE));
            @SuppressLint("Range") String name =cursor.getString(cursor.getColumnIndex(DbHelper.NAME));
            @SuppressLint("Range") String password =cursor.getString(cursor.getColumnIndex(DbHelper.PASSWORD));
            buffer.append(cid+ "   " + privilege + "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }

    public int delete(String uname)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] whereArgs ={uname};
        int count =db.delete(DbHelper.TABLE_NAME ,DbHelper.NAME+" = ?",whereArgs);
        return count;
    }

    public int updateName(String oldName , String newName, String newPassword)
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.NAME,newName);
        contentValues.put(DbHelper.PASSWORD,newPassword);
        String[] whereArgs= {oldName};
        int count =db.update(DbHelper.TABLE_NAME,contentValues, DbHelper.NAME+" = ?",whereArgs );
        return count;
    }

    public Boolean fetchUser(String username, String userpassword){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Users where Name = ? and Password = ?", new String[] {username,userpassword});
        if (cursor.getCount()>0){
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkDuplicates(String username){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Users where Name = ?", new String[] {username});
        if (cursor.getCount()>0){
            return true;
        } else {
            return false;
        }
    }

    public String checkPrivilege(String username, String userpassword){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Users where Name = ? and Password = ?", new String[] {username,userpassword});
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())   {
            @SuppressLint("Range") String privilege =cursor.getString(cursor.getColumnIndex(DbHelper.PRIVILEGE));
            buffer.append(privilege);
        }
        return buffer.toString();
    }


    public String getUserPrivilege(String username){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Users where Name = ?", new String[] {username});
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())   {
            @SuppressLint("Range") String privilege =cursor.getString(cursor.getColumnIndex(DbHelper.PRIVILEGE));
            buffer.append(privilege);
        }
        return buffer.toString();
    }

    static class DbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "LoginDatabase";    // Database Name
        private static final String TABLE_NAME = "Users";   // Table Name
        private static final int DATABASE_Version = 1;   // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String PRIVILEGE="Privilege";     // Column II
        private static final String NAME = "Name";    //Column III
        private static final String PASSWORD= "Password";    // Column IV
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+PRIVILEGE+" VARCHAR(255) ,"+NAME+" VARCHAR(255) ,"+ PASSWORD+" VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                ToastNotif.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                ToastNotif.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
                ToastNotif.message(context,""+e);
            }
        }
    }
}
