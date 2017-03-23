package com.haoye.dartreader.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoye.dartreader.R;
import com.haoye.dartreader.file.data.ImportedBooks;

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
    private static ArrayList<Book> importedbooks;

    public BookListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        importedbooks = ImportedBooks.getImportedBookList(context);
    }

    protected static ArrayList<Book> getBookList() {
        return importedbooks;
    }

    public static boolean contains(String filePath) {
        for (Book book : importedbooks) {
            if (book.getPath().equals(filePath)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        importedbooks.clear();
        notifyDataSetChanged();
    }

    public Book remove(int index) {
        Book book = importedbooks.remove(index);
        notifyDataSetChanged();
        return book;
    }

    public void remove(Book book) {
        importedbooks.remove(book);
        notifyDataSetChanged();
    }

    public boolean addBook(Book book) {
        boolean success = importedbooks.add(book);
        notifyDataSetChanged();
        return success;
    }

    public void addBooks(ArrayList<Book> bookList) {
        importedbooks.addAll(bookList);
        notifyDataSetChanged();
    }

    public Book getBook(int position) {
        return importedbooks.get(position);
    }

    @Override
    public int getCount() {
        return importedbooks.size();
    }

    @Override
    public Object getItem(int position) {
        return importedbooks.get(position);
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

        Book book = importedbooks.get(position);
        holder.bookName.setText(book.getName());
        return convertView;
    }

    private class ViewHolder {
        private TextView bookName;
    }
}
