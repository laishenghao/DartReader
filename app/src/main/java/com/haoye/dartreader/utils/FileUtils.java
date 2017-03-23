package com.haoye.dartreader.utils;

import android.os.Environment;
import android.util.Log;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static String getSdCardPath() {
        String path = "/";
        File file = new File(Environment.getExternalStorageDirectory(), "_test.txt");
        if (file.exists()) {
            path = file.getAbsolutePath();
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
        String type = getEncodingType(path);
        File file = new File(path);
        StringBuilder builder = new StringBuilder((int)file.length());
        try {
            InputStream stream = new FileInputStream(file);

            InputStreamReader streamReader   = new InputStreamReader(stream, type);
            BufferedReader    bufferedReader = new BufferedReader(streamReader);
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                if (++count > 10) {
                    break;
                }
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static String getEncodingType(String path){
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        try {
            FileInputStream fis = new FileInputStream(path);
            int read;
            while ((read = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return encoding;
    }

}
