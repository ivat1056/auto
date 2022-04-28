package com.example.auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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

public class ConPersonal extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd , btnClear;
    EditText etDol, etFio, etXar;

    DBHelper2 dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_personal);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        etDol = (EditText) findViewById(R.id.etDol);
        etFio = (EditText) findViewById(R.id.etFio);
        etXar = (EditText) findViewById(R.id.etXar);

        dbHelper = new DBHelper2(this);
        database = dbHelper.getWritableDatabase();


        UpdateTable();

        etDol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etDol.setHint("");
                else
                    etDol.setHint("ФИО");
            }
        });
        etFio.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etFio.setHint("");
                else
                    etFio.setHint("Телефон");
            }
        });
        etXar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    etXar.setHint("");
                else
                    etXar.setHint("Адрес");
            }
        });
    }
    public void UpdateTable() {
        Cursor cursor = database.query(DBHelper2.TABLE_EMPLOEE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {

            int idIndex = cursor.getColumnIndex(DBHelper2.KEY_ID1);
            int nameIndex = cursor.getColumnIndex(DBHelper2.KEY_DOL);
            int mailIndex = cursor.getColumnIndex(DBHelper2.KEY_FIO);
            int adresIndex = cursor.getColumnIndex(DBHelper2.KEY_XAR);
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


                String name = etDol.getText().toString();
                String email = etFio.getText().toString();
                String adres = etXar.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper2.KEY_DOL, name);
                contentValues.put(DBHelper2.KEY_FIO, email);
                contentValues.put(DBHelper2.KEY_XAR, adres);


                database.insert(DBHelper2.TABLE_EMPLOEE, null, contentValues);
                etDol.setText("");
                etFio.setText("");
                etXar.setText("");
                UpdateTable();
                break;


            case R.id.btnClear:
                database.delete(DBHelper2.TABLE_EMPLOEE, null, null);

                TableLayout dbOutput = findViewById(R.id.dbOutput);
                dbOutput.removeAllViews();
                UpdateTable();
                break;
            default:
                View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();


                database.delete(DBHelper2.TABLE_EMPLOEE, DBHelper2.KEY_ID1 + " = ?", new String[]{String.valueOf((v.getId()))});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper2.TABLE_EMPLOEE, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_ID1);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_DOL);
                    int mailIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_FIO);
                    int adresIndex = cursorUpdater.getColumnIndex(DBHelper2.KEY_XAR);
                    int realID = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realID) {
                            contentValues.put(DBHelper2.KEY_ID1, realID);
                            contentValues.put(DBHelper2.KEY_ID1, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper2.KEY_ID1, cursorUpdater.getString(mailIndex));
                            contentValues.put(DBHelper2.KEY_ID1, cursorUpdater.getString(adresIndex));
                            database.replace(DBHelper2.TABLE_EMPLOEE, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper2.TABLE_EMPLOEE, DBHelper2.KEY_ID1 + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                break;
        }
    }
    public void operation4(View view) {

        Intent intent = new Intent(this, admin_login.class);
        startActivity(intent);
    }
}