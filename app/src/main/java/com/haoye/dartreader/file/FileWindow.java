package com.haoye.dartreader.file;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoye.dartreader.R;
import com.haoye.dartreader.book.Book;
import com.haoye.dartreader.book.BookListAdapter;
import com.haoye.dartreader.book.imp.BookDefault;
import com.haoye.dartreader.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
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
    private String currentPath = FileUtils.getSdCardPath();
    private FileListAdapter fileListAdapter;
    private OnFinishedListener onFinishedListener;
    private ArrayList<TypeFile> fileList = new ArrayList<>();
    private ArrayList<Book> selectedBooks = new ArrayList<>();

    public FileWindow(Context context) {
        super(context);
        selectedBooks.clear();
        initControlButtons();
    }

    public boolean isSelected(String path) {
        for (Book book : selectedBooks) {
            if (book.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public void setOnFinishedListener(OnFinishedListener finishedListener) {
        this.onFinishedListener = finishedListener;
    }

    private void initControlButtons() {
        fileListAdapter = new FileListAdapter(getContext());
//        fileListAdapter.update("/");
        fileListAdapter.update(FileUtils.getSdCardPath());
        this.setAdapter(fileListAdapter, this);
        this.setTitle("请选择书籍");
        this.setPositiveButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDismiss(dialog, true);
                dialog.dismiss();
                if (onFinishedListener != null) {
                    onFinishedListener.onFinished(selectedBooks);
                }
            }
        });
        this.setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setDismiss(dialog, false);

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

        TypeFile typeFile = fileListAdapter.getFile(which);
        String path = typeFile.getFile().getPath();
        switch (typeFile.getType()) {
            case TypeFile.TYPE_IS_DIR:
                fileListAdapter.update(path);
                currentPath = path;
                break;
            case TypeFile.TYPE_IMPORTED:
                break;
            case TypeFile.TYPE_SELECTED:
                typeFile.setType(TypeFile.TYPE_RAW);
                for (Book book : selectedBooks) {
                    if (book.getPath().equals(path)) {
                        selectedBooks.remove(book);
                        break;
                    }
                }
                break;
            default:
                typeFile.setType(TypeFile.TYPE_SELECTED);
                selectedBooks.add(new BookDefault(path));
                break;
        }
        fileListAdapter.notifyDataSetChanged();
    }

    public interface OnFinishedListener {
        void onFinished(ArrayList<Book> books);
    }


    public class FileListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public FileListAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
        }

        public void clear() {
            fileList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        private void fastUpdate(ArrayList<TypeFile> dirs, ArrayList<TypeFile> files) {
            fileList.clear();
            fileList.addAll(dirs);
            fileList.addAll(files);
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

            if (files == null || files.length == 0) {
                this.clear();
                return;
            }
            ArrayList<TypeFile> dirsList = new ArrayList<>();
            ArrayList<TypeFile> fileList = new ArrayList<>();
            for (File file : files) {
                TypeFile typeFile = new TypeFile(file);
                if (file.isDirectory()) {
                    typeFile.setType(TypeFile.TYPE_IS_DIR);
                    dirsList.add(typeFile);
                }
                else {
                    if (BookListAdapter.contains(file.getPath())){
                        typeFile.setType(TypeFile.TYPE_IMPORTED);
                    }
                    else if (isSelected(file.getPath())) {
                        typeFile.setType(TypeFile.TYPE_SELECTED);
                    }
                    fileList.add(typeFile);
                }
            }

            fastUpdate(dirsList, fileList);
        }

        public TypeFile getFile(int index) {
            if (index < 0 || index >= fileList.size()) {
                return null;
            }
            return fileList.get(index);
        }

        @Override
        public Object getItem(int position) {
            if (position >= fileList.size()) {
                return null;
            }
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TypeFile typeFile = fileList.get(position);
            File file = typeFile.getFile();
            if (file.isDirectory()) {
                convertView = inflater.inflate(R.layout.file_list_item_folder, null);
                TextView name = (TextView) convertView.findViewById(R.id.itemFolderNameTxtV);
                name.setText(file.getName());
            }
            else {
                convertView = inflater.inflate(R.layout.file_list_item_file, null);
                TextView name = (TextView) convertView.findViewById(R.id.itemFileNameTxtV);
                name.setText(file.getName());
                ImageView statusImg = (ImageView) convertView.findViewById(R.id.itemFileStatusImg);
                switch (typeFile.getType()) {
                    case TypeFile.TYPE_RAW:
                        statusImg.setVisibility(View.GONE);
                        break;
                    case TypeFile.TYPE_IMPORTED:
                        statusImg.setVisibility(View.VISIBLE);
                        statusImg.setAlpha(0.1f);
                        break;
                    case TypeFile.TYPE_SELECTED:
                        statusImg.setVisibility(View.VISIBLE);
                        statusImg.setAlpha(1.0f);
                        break;
                    default:
                        break;
                }
            }
            return convertView;
        }

    }

}
