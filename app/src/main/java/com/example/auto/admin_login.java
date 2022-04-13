package com.example.auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class admin_login extends AppCompatActivity implements View.OnClickListener {
    Button  btnAdd , btnClear;
    EditText etName, etEmail, etAdres;

    DBHelper2 dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

       btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAdres = (EditText) findViewById(R.id.etAdres);

        dbHelper = new DBHelper2(this);
        database = dbHelper.getWritableDatabase();


        UpdateTable();

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etName.setHint("");
                else
                    etName.setHint("ФИО");
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etEmail.setHint("");
                else
                    etEmail.setHint("Телефон");
            }
        });
        etAdres.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etAdres.setHint("");
                else
                    etAdres.setHint("Адрес");
            }
        });
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper2.TABLE_CONTACTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DBHelper2.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper2.KEY_NAME);
            int mailIndex = cursor.getColumnIndex(DBHelper2.KEY_MAIL);
            int adresIndex = cursor.getColumnIndex(DBHelper2.KEY_ADRES);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do {
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);


                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputMail = new TextView(this);
                params.weight = 3.0f;
                outputMail.setLayoutParams(params);
                outputMail.setText(cursor.getString(mailIndex));
                dbOutputRow.addView(outputMail);

                TextView outputAdres = new TextView(this);
                params.weight = 3.0f;
                outputAdres.setLayoutParams(params);
                outputAdres.setText(cursor.getString(adresIndex));
                dbOutputRow.addView(outputAdres);


                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить запись");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);


                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());

        }

        cursor.close();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.btnAdd:


                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String adres = etAdres.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper2.KEY_NAME, name);
                contentValues.put(DBHelper2.KEY_MAIL, email);
                contentValues.put(DBHelper2.KEY_ADRES, adres);


                database.insert(DBHelper2.TABLE_CONTACTS, null, contentValues);
                etName.setText("");
                etEmail.setText("");
                etAdres.setText("");
                UpdateTable();
                break;


            case R.id.btnClear:
                database.delete(DBHelper2.TABLE_CONTACTS, null, null);

                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();


                database.delete(DBHelper2.TABLE_CONTACTS, DBHelper2.KEY_ID + " = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper2.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_NAME);
                    int mailIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_MAIL);
                    int adresIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_ADRES);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper2.KEY_ID, realID);
                            contentValues.put(DBHelper2.KEY_ID, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper2.KEY_ID, cursorUpdater.getString(mailIndex));
                            contentValues.put(DBHelper2.KEY_ID, cursorUpdater.getString(adresIndex));
                            database.replace(DBHelper2.TABLE_CONTACTS, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper2.TABLE_CONTACTS, DBHelper2.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
}