package ru.saperov.fourpda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saperov on 23.03.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE = "news";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_HREF = "href";
    public static final String COLUMN_DESC = "desc";
    public static final String COLUMN_SRC_IMG = "srcImg";
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_HREF + " text, "
            + COLUMN_DESC + " text, "
            + COLUMN_SRC_IMG + " text"
            + ");";

 /*   public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }

    public void addNews(DbWrapper dbWrapper){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, dbWrapper.getTitle());
        values.put(COLUMN_HREF, dbWrapper.getHref());
        values.put(COLUMN_DESC, dbWrapper.getDesc());
        values.put(COLUMN_SRC_IMG, dbWrapper.getSrcImg());
        // Вставляем строку в таблицу
        db.insert(DATABASE_TABLE, null, values);
        db.close();
    }

    public List<DbWrapper> getAllNews(){
        ArrayList<DbWrapper> newsList = new ArrayList<DbWrapper>();
        //выбираем всю таблицу
        String selectQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Проходим по всем строкам и добавляем в список
        if (cursor.moveToFirst()) {
            do {
                DbWrapper news = new DbWrapper();
                news.setID(Integer.parseInt(cursor.getString(0)));
                news.setTitle(cursor.getString(1));
                news.setHref(cursor.getString(2));
                news.setDesc(cursor.getString(3));
                news.setSrcImg(cursor.getString(4));
                newsList.add(news);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return newsList;
    }

    //получить число новостей
    public int getNewsCount(){
        String countQuery = "SELECT * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    //удалить все новости
    public void delAllNews(){
        SQLiteDatabase db = this.getWritableDatabase();
        String delQuery = "DELETE FROM " + DATABASE_TABLE +";";
        db.execSQL(delQuery);
        db.close();
    }

    //удалить таблицу и создать
    public void upgradeNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        String delQuery = "DROP TABLE  " + DATABASE_TABLE+";";
        // Удаляем старую таблицу и создаём новую
        db.execSQL(delQuery);
        // Создаём новую таблицу
        onCreate(db);
    }
}
