package com.haoye.dartreader.file;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.haoye.dartreader.book.Book;
import com.haoye.dartreader.book.imp.BookDefault;
import com.haoye.dartreader.utils.FileUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public class FileWindow extends AlertDialog.Builder implements DialogInterface.OnClickListener {
    private String currentPath = FileUtils.getPublicDocumentPath();
    private ArrayList<Book> importBooks = new ArrayList<>();
    private FileListAdapter fileListAdapter;
    private OnFinishedListener onFinishedListener;


    public FileWindow(Context context) {
        super(context);
        importBooks.clear();
        initControlButtons();
    }

    public void setOnFinishedListener(OnFinishedListener finishedListener) {
        this.onFinishedListener = finishedListener;
    }

    private void initControlButtons() {
        fileListAdapter = new FileListAdapter(getContext());
        fileListAdapter.update("/");
        this.setAdapter(fileListAdapter, this);
        this.setNegativeButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDismiss(dialog, true);
                dialog.dismiss();
                if (onFinishedListener != null) {
                    onFinishedListener.onFinished(importBooks);
                }
            }
        });
        this.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDismiss(dialog, false);// 不退出

                File file = new File(currentPath);
                String path = file.getParent();
                if (path != null) {
                    currentPath = path;
                    fileListAdapter.update(currentPath);
                }
            }
        });
    }

    private void setDismiss(DialogInterface dialog, boolean flag) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        setDismiss(dialog, false);

        File file = fileListAdapter.getFile(which);
        String path = file.getPath();
        if (file.isDirectory()) {
            fileListAdapter.update(path);
            currentPath = path;
        }
        else {
            importBooks.add(new BookDefault(path));
        }
    }

    public interface OnFinishedListener {
        void onFinished(ArrayList<Book> books);
    }
}
