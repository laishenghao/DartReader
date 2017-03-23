package com.haoye.dartreader.file;

import java.io.File;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-23
 * @see
 */

public class TypeFile {
    public static final int TYPE_IS_DIR = -1;
    public static final int TYPE_RAW = 0;
    public static final int TYPE_SELECTED = 1;
    public static final int TYPE_IMPORTED = 2;
    private int  type = TYPE_RAW;
    private File file;

    public TypeFile(File file) {
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }


}
