package com.haoye.dartreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.haoye.dartreader.book.Book;
import com.haoye.dartreader.book.BookListView;
import com.haoye.dartreader.file.FileWindow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BookListView bookListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        bookListView = (BookListView) findViewById(R.id.bookListView);

        Button importBookBtn = (Button) findViewById(R.id.importBookBtn);
        importBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileWindow fileWindow = new FileWindow(MainActivity.this);
                fileWindow.create().show();
                fileWindow.setOnFinishedListener(new FileWindow.OnFinishedListener() {
                    @Override
                    public void onFinished(ArrayList<Book> books) {
                        if (books.size() > 0) {
                            bookListView.addBooks(books);
                        }
                    }
                });

            }

        });
    }



}
