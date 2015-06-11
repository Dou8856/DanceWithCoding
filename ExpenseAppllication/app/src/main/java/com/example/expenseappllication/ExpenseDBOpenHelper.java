package com.example.expenseappllication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 23060814 on 6/11/15.
 */
public class ExpenseDBOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "ExpenseRecording";
    private final static int DB_VERSION = 2;
    private final static String DB_CREATE = "Create table weekly expense";
    public ExpenseDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
