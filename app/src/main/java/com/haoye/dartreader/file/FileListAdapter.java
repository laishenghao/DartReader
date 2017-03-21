package com.haoye.dartreader.file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoye.dartreader.R;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-01
 * @see
 */

public class FileListAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private File[] files = null;

    public FileListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (files == null) {
            return 0;
        }
        return files.length;
    }

    public void update(File[] files) {
        this.files = files;
        notifyDataSetChanged();
    }

    public void update(String folderPath) {
        File[] files = new File(folderPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory()
                        || pathname.getName().endsWith(".txt")
                        || pathname.getName().endsWith(".TXT");
            }
        });
        update(files);
    }

    public File getFile(int index) {
        if (index < 0 || index >= files.length) {
            return null;
        }
        return files[index];
    }

    @Override
    public Object getItem(int position) {
        if (files == null || position >= files.length) {
            return null;
        }
        return files[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (files[position].isDirectory()) {
            convertView = inflater.inflate(R.layout.file_list_item_folder, null);
            TextView textView = (TextView) convertView.findViewById(R.id.itemFolderNameTxtV);
            textView.setText(files[position].getName());
        }
        else {
            convertView = inflater.inflate(R.layout.file_list_item_file, null);
            TextView textView = (TextView) convertView.findViewById(R.id.itemFileNameTxtV);
            textView.setText(files[position].getName());
        }
        return convertView;
    }


}
