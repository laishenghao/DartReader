package com.haoye.dartreader.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoye.dartreader.R;

import java.util.ArrayList;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public class BookListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Book> books = new ArrayList<>();

    public BookListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void clear() {
        this.books.clear();
    }

    public Book remove(int index) {
        return this.books.remove(index);
    }

    public void remove(Book book) {
        this.books.remove(book);
    }

    public boolean addBook(Book book) {
        boolean success = this.books.add(book);
        notifyDataSetChanged();
        return success;
    }

    public void addBooks(ArrayList<Book> books) {
        this.books.addAll(books);
        notifyDataSetChanged();
    }

    public Book getBook(int position) {
        return this.books.get(position);
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.book_list_item_default, null);
            holder = new ViewHolder();
            holder.bookName = (TextView) convertView.findViewById(R.id.itemBookNameTxtV);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = books.get(position);
        holder.bookName.setText(book.getName());
        return convertView;
    }

    private class ViewHolder {
        private TextView bookName;
    }
}
