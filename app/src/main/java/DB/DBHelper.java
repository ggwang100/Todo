package DB;

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
        String memoSQL = "CREATE TABLE TODO (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT NOT NULL," +
                "CONTENT TEXT," +
                "CREATE_DATE DATE NOT NULL," +
                "ALARM_DATE TEXT," +
                "ALARM_TIME TEXT" +
                ")";
        sqLiteDatabase.execSQL(memoSQL);
}

    public void onInsert(String TITLE, String CONTENT, String ALARM_DATE, String ALARM_TIME){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO TODO VALUES(NULL, '" + TITLE + "', '" + CONTENT + "', DATE('NOW'), '" + ALARM_DATE + "', '" + ALARM_TIME + "')";
        Log.d("insert", "onInsert: " + sql);
        db.execSQL(sql);
    }

    public void onDelete(int id){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM TODO WHERE _id=" + id;
        Log.d("delete", "ondelete: " + sql);
        db.execSQL(sql);
    }

    public void onUpdate(int id, String TITLE, String CONTENT, String ALARM_DATE, String ALARM_TIME){
        SQLiteDatabase db = getWritableDatabase();

        String sql = "UPDATE TODO SET TITLE = '" + TITLE  + "', CONTENT = '" + CONTENT + "', ALARM_DATE = '" + ALARM_DATE + "', ALARM_TIME = '" + ALARM_TIME + "' WHERE _id = " + id;
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(newVersion == DATABASE_VEPSION){
            sqLiteDatabase.execSQL("drop table tb_memo");
            onCreate(sqLiteDatabase);
        }
    }
}
