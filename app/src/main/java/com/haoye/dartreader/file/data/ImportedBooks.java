package com.haoye.dartreader.file.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.haoye.dartreader.book.Book;
import com.haoye.dartreader.book.imp.BookDefault;

import java.util.ArrayList;

public class ImportedBooks {
    public static final String TABLE_NAME_IMPORTED_BOOK = "imported_book_list_tb";
    private static final String BOOK_POSITION = "position";
    private static final String BOOK_PREFERENCES = "book_preferences";
    private static final String DATABASE_NAME = "book_db";

    public static ArrayList<Book> getImportedBookList(Context paramContext) {
        ArrayList<Book> bookList = new ArrayList<>();
        SQLiteDatabase database = new MyOpenHelper(paramContext, DATABASE_NAME, null, 1).getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_IMPORTED_BOOK, null);

        while (cursor.moveToNext()) {
            BookDefault item = new BookDefault();
            item.setName(cursor.getString(cursor.getColumnIndex("name")));
            item.setPath(cursor.getString(cursor.getColumnIndex("path")));
            item.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
            item.setType(cursor.getString(cursor.getColumnIndex("type")));
            item.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
            bookList.add(item);
        }
        cursor.close();
        database.close();
        return bookList;
    }

    public static int getNewBookPosition(Context context) {
        return context.getSharedPreferences(BOOK_PREFERENCES, 0).getInt("position", -1);
    }

    public static void putModifyBook(Context context, Book book, int position) {
        SharedPreferences.Editor editor = context.getSharedPreferences(BOOK_PREFERENCES, 0).edit();
        editor.putString("name", book.getName());
        editor.putString("path", book.getPath());
        editor.putString("author", book.getAuthor());
        editor.putString("type", book.getType());
        editor.putInt("progress", book.getProgress());
        editor.apply();
    }

    public static void saveBookList(Context context, ArrayList<Book> bookList) {
        MyOpenHelper helper = new MyOpenHelper(context, DATABASE_NAME, null, 1);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(TABLE_NAME_IMPORTED_BOOK, null, null);
        helper.createTable(database, TABLE_NAME_IMPORTED_BOOK);
        for (int i = 0; i < bookList.size(); i++) {
            Book item = bookList.get(i);
            ContentValues values = new ContentValues();
            values.put("name", item.getName());
            values.put("path", item.getPath());
            values.put("author", item.getAuthor());
            values.put("type", item.getType());
            values.put("progress", item.getProgress());
            database.insert(TABLE_NAME_IMPORTED_BOOK, null, values);
        }
        database.close();
    }

//    public static void setNewBookPosition(Context context, int position) {
//        SharedPreferences.Editor editor = context.getSharedPreferences("book_modify", 0).edit();
//        editor.putInt("position", position);
//        editor.apply();
//    }

    private static class MyOpenHelper extends SQLiteOpenHelper {
        public MyOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void createTable(SQLiteDatabase database, String tableName) {
            database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
                    + "(_id integer primary key autoincrement, " + "name" + ", "
                    + "path" + ", " + "author" + ", " + "type" + ", " + "progress" + ")");
        }

        public void onCreate(SQLiteDatabase database) {
            createTable(database, TABLE_NAME_IMPORTED_BOOK);
        }

        public void onUpgrade(SQLiteDatabase database, int paramInt1, int paramInt2) {
            Log.w("Database", "Version Change:" + paramInt1 + "--->" + paramInt2);
        }
    }
}