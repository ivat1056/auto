package com.example.auto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Con_for_Cl extends AppCompatActivity {
    DBHelper2 dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_for_cl);
        dbHelper = new DBHelper2(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
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




                dbOutput.addView(dbOutputRow);
            } while (cursor.moveToNext());

        }

        cursor.close();
    }

    public void operation13(View view) {

        Intent intent = new Intent(this, Client.class);
        startActivity(intent);
    }
}