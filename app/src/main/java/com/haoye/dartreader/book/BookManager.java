package com.haoye.dartreader.book;

import android.content.Context;
import android.content.Intent;

import com.haoye.dartreader.BookActivity;

/**
 * @brief
 * @detail
 * @see
 * @author Haoye
 * @date 2017-03-20
 */
public class BookManager {
    private static Book openedBook = null;

    public static Book getOpenedBook() {
        return openedBook;
    }

    public static void open(Context context, Book book) {
        openedBook = book;
        Intent intent = new Intent(context, BookActivity.class);
        context.startActivity(intent);
    }

    public static void releaseOpenBook() {
        openedBook = null;
    }

}
