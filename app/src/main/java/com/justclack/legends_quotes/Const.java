package com.justclack.legends_quotes;

public class Const {

    public static final String DATABASE_NAME = "offline.sql";
    public static final String FAV_TABLE_NAME = "fav";
    public static final String id = "id";
    public static final String title = "title";
    public static final String fav_table_query = "CREATE TABLE IF NOT EXISTS " + FAV_TABLE_NAME + "(" + id + " INTEGER PRIMARY KEY AUTOINCREMENT ," + title + " VARCHAR)";
}