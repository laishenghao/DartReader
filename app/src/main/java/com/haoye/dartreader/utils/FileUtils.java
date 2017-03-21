package com.haoye.dartreader.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @brief
 * @detail
 * @see
 * @author Haoye
 * @date 2017-03-20
 */

public class FileUtils {

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static String getPublicDocumentPath() {
        String path = "/";
        if (isExternalStorageReadable()) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();
        }
        return path;
    }


    public static byte[] readFile(String path) {
        try {
            File file = new File(path);
            FileInputStream stream = new FileInputStream(file);
            byte[] bytes = new byte[stream.available()];
            stream.read(bytes);
            stream.close();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int pump(InputStream in, byte[] out) {
        int total = 0;
        try {
            while (total < out.length) {
                int read = in.read(out, total, out.length - total);
                if (read == -1) {
                    break;
                }
                total += read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static String pump(String path) {
        Log.e("pump", path);
        StringBuilder builder = new StringBuilder();
        try {
            InputStream       stream         = new FileInputStream(new File(path));
            InputStreamReader streamReader   = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader    bufferedReader = new BufferedReader(streamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

}
