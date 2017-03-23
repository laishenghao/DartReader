package com.haoye.dartreader.book;

import android.content.Context;
import android.content.Intent;

import com.haoye.dartreader.BookActivity;
import com.haoye.dartreader.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.haoye.dartreader.utils.FileUtils.getEncodingType;

/**
 * @brief
 * @detail
 * @see
 * @author Haoye
 * @date 2017-03-20
 */
public class OpenedBookManager {
    private static long readCount = 0;
    private static long totalCount = 0;
    private static String originalText;
    private static Book   openedBook = null;
    private static BufferedReader reader = null;

    public static Book getOpenedBook() {
        return openedBook;
    }

    public static void open(Context context, Book book) {
        openedBook   = book;
        reader       = createReader();
//        originalText = pump(2000);
        originalText = pumpAll();
        readCount = originalText.getBytes().length;
        Intent intent = new Intent(context, BookActivity.class);
        context.startActivity(intent);
    }

    private static BufferedReader createReader() {
        File file = new File(openedBook.getPath());
        totalCount = file.length();
        try {
            InputStream stream = new FileInputStream(file);
            String type = getEncodingType(file.getPath());
            InputStreamReader streamReader = new InputStreamReader(stream, type);
            return new BufferedReader(streamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOriginalText() {
        return originalText;
    }

    public static String getPatternString(String mask, int start, int interval) {
        StringBuilder builder = new StringBuilder(originalText.length() * 3);
        if (start > 0) {
            builder.append(originalText.substring(0, start));
        }
        for (int i = start+1; i+1+interval < originalText.length(); i += interval) {
            builder.append(mask);
            String temp = originalText.substring(i, i+interval);
            builder.append(temp);
        }
        return builder.toString();
    }

    public static String pumpAll() {
        StringBuilder builder = new StringBuilder((int) totalCount);
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static String pump(int limit) {
        StringBuilder builder = new StringBuilder(limit+10);
        try {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null && count < limit) {
                builder.append(line);
                count += line.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static boolean canReplace(String letter) {
        String punctuation = "`1234567890-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?·~！@#￥%……&*（）——+【】、；‘，。/{}：“”《》？";
        if (punctuation.contains(letter)) {
            return false;
        }
        return true;
    }

    public static void release() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader = null;
        }
    }

}
