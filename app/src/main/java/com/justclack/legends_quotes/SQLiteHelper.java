package com.justclack.legends_quotes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


/**
 * Created by Mujahid on 2/23/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context) {
        super(context, Const.DATABASE_NAME, null, 1);
    }

    public void createTable(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }


    public void AddToFav(String title) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO " + Const.FAV_TABLE_NAME + " VALUES(NULL,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, title);
        statement.executeInsert();
    }



    public void deleteData(String title) {
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM " + Const.FAV_TABLE_NAME + " WHERE " + Const.title + "= ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, title);
        statement.execute();
        database.close();

    }

    public Cursor getData(String sql) {

        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}