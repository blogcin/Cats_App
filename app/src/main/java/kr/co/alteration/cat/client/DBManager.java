package kr.co.alteration.cat.client;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by blogc on 2016-09-24.
 */
public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE DATE( date TEXT, sec TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void add(String date, String sec) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "INSERT INTO DATE values(\"" + date + "\", \"" + sec + "\");";
        db.execSQL(query);
        db.close();
    }

    public void clear() {
        SQLiteDatabase db = getWritableDatabase();

        String sql = "DELETE FROM DATE ";
        db.execSQL(sql);
        db.close();
    }

    // date -> yyyy/mm/dd
    public String get(String date) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * from DATE WHERE date=\"" + date + "\"";
        Log.d("Query", query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() != 0) {
            String str = "";

            while(cursor.moveToNext()) {
                str += (cursor.getString(1) + " ");
            }

            Log.d("Query", "Result : " + str);
            return str;
        } else {
            return null;
        }

    }

}
