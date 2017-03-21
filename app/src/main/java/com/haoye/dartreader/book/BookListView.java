package com.haoye.dartreader.book;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public class BookListView extends ListView implements AdapterView.OnItemClickListener {
    private BookListAdapter adapter;

    public BookListView(Context context) {
        this(context, null);
    }

    public BookListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void addBooks(ArrayList<Book> books) {
        adapter.addBooks(books);
    }

    public void addBook(Book book) {
        adapter.addBook(book);
    }

    private void init(Context context) {
        adapter = new BookListAdapter(context);
        this.setAdapter(adapter);
        this.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookManager.open(getContext(), adapter.getBook(position));
    }
}
