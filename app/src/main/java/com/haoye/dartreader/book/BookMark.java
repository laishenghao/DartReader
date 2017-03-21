package com.haoye.dartreader.book;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-20
 * @see
 */

public interface BookMark {
    int TYPE_UNDERLINE = 1;

    int getStart();

    int getEnd();

    int getType();
}
