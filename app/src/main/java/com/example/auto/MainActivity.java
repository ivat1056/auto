package com.example.auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView usernameField, passwordField;
    Button loginBtn, signinBtn;
    DBHelper dbHelper;
    SQLiteDatabase database;
    String adminL = "admin";
    String adminP = "1234";
    public static String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);

        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        signinBtn = findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(this);




        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_NAME, adminL);
        contentValues.put(DBHelper.KEY_PASSWORD, adminP);

        database.insert(DBHelper.TABLE_USERS, null, contentValues);

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.loginBtn:
                Cursor logCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean logged = false;
                if(logCursor.moveToFirst()){

                    int usernameIndex = logCursor.getColumnIndex(DBHelper.KEY_NAME);
                    int passwordIndex = logCursor.getColumnIndex(DBHelper.KEY_PASSWORD);

                    do{
                        if ((usernameField.getText().toString().equals(adminL)) && (passwordField.getText().toString().equals(adminP)))
                        {
                            startActivity(new Intent(this,admin_login.class));
                            logged=true;
                            break;
                        }
                        if(usernameField.getText().toString().equals(logCursor.getString(usernameIndex)) && passwordField.getText().toString().equals(logCursor.getString(passwordIndex)))
                        {
                            user = logCursor.getString(usernameIndex);
                            startActivity(new Intent(this,Client.class));
                            logged=true;
                            break;
                        }
                    }while ( logCursor.moveToNext());

                }
                logCursor.close();
                if(!logged) Toast.makeText(this,"Введенная комбинация логина и пароля не была найдена.",Toast.LENGTH_LONG).show();
                break;
            case R.id.signinBtn:
                Cursor signCursor = database.query(DBHelper.TABLE_USERS,null, null, null, null, null, null);
                boolean finded = false;
                if(signCursor.moveToFirst())
                {
                    int usernameIndex = signCursor.getColumnIndex(DBHelper.KEY_NAME);
                    do{
                        if(usernameField.getText().toString().equals(signCursor.getString(usernameIndex))){
                            Toast.makeText(this,"Введенный вами логин уже зарегистрирован.",Toast.LENGTH_LONG).show();
                            finded =true;
                            break;
                        }
                    } while (signCursor.moveToNext());
                }
                if (!finded)
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAME, usernameField.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD,passwordField.getText().toString());
                    database.insert(DBHelper.TABLE_USERS,null,contentValues);
                    Toast.makeText(this,"Вы успешно зарегистрировались!",Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }


    }

}