package com.haoye.dartreader.book;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haoye.dartreader.file.data.ImportedBooks;

import java.util.ArrayList;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public class BookListView extends ListView
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
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

    public void saveImportedBooksToDb() {
        ImportedBooks.saveBookList(getContext(), BookListAdapter.getBookList());
    }

    private void init(Context context) {
        adapter = new BookListAdapter(context);
        this.setAdapter(adapter);
        this.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        this.setOnItemClickListener(this);
        this.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OpenedBookManager.open(getContext(), adapter.getBook(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(new CharSequence[]{"删除此书", "清空书架", "取消"},
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                    case 0:
                        adapter.remove(position);
                        break;
                    case 1:
                        adapter.clear();
                        break;
                    default:
                        break;
                    }
                }
            });
        builder.show();
        return true;
    }

}
