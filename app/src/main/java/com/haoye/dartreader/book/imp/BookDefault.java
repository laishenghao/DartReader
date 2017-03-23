package com.haoye.dartreader.book.imp;

import com.haoye.dartreader.book.Book;

import java.io.File;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public class BookDefault implements Book {
    private String name = "";
    private String path = "";
    private String author = "";
    private String type = "";
    private int progress = 0;

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public BookDefault() {

    }

    public BookDefault(String path) {
        setPath(path);
        setNameByPath(path);
    }

    public void setNameByPath(String path) {
        int begin = path.lastIndexOf(File.separator) + 1;
        int end   = path.lastIndexOf('.');
        String name = path.substring(begin, end);
        this.setName(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getLength() {
        return null;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Book)) {
            return false;
        }
        return getPath().equals(((Book) obj).getPath());
    }
}
