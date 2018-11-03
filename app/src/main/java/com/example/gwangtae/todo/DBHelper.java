package com.example.gwangtae.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VEPSION = 1;
    public DBHelper(Context context){
        super(context,"memodb",null,DATABASE_VEPSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String memoSQL = "CREATE TABLE tb_memo (" +
                "_ID INT PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT NOT NULL," +
                "CONTENT TEXT," +
                "CREATE_DATE DATE NOT NULL," +
                "TIME TEXT NOT NULL," +
                "ALARM TEXT" +
                ")";
        sqLiteDatabase.execSQL(memoSQL);
}

    public void onInsert(String title, String content){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_TABLE = "insert into tb_memo values (null, '" + title + "', '" + content + "', date('now'))";
        Log.d("insert", "onInsert: " + INSERT_TABLE);
        db.execSQL(INSERT_TABLE);
    }

    public void onDelete(int id){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "delete from tb_memo where _id=" + id;
        db.execSQL(sql);
    }

    public void onUpdate(int id, String title, String content){
        SQLiteDatabase db = getWritableDatabase();

        String INSERT_TABLE = "update tb_memo set title='" + title + "', content='" + content + "' where _id = " + id;
        db.execSQL(INSERT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(newVersion == DATABASE_VEPSION){
            sqLiteDatabase.execSQL("drop table tb_memo");
            onCreate(sqLiteDatabase);
        }


    }
}