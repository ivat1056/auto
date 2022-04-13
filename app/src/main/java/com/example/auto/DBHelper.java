package com.example.auto;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

import androidx.annotation.Nullable;

public class DBHelper  extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "usersDB";
    public static final String TABLE_USERS = "usersTable";

    public static final String KEY_ID="_id";
    public static final String KEY_NAME="name";
    public static final String KEY_PASSWORD="password";

    public DBHelper(@Nullable Context context)
    {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS+ "("
                +KEY_ID+ "integer primary key,"
                +KEY_NAME+ " text,"
                +KEY_PASSWORD+ " text"+
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        if(i1>i){
            db.execSQL("drop table if exists "+TABLE_USERS);
            onCreate(db);
        }
    }
}
