package com.haoye.dartreader.book;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @brief
 * @detail
 * @see LinearLayout
 * @author Haoye
 * @date 2017/3/21
 */
public class BookTitlebar extends LinearLayout {

    private int selectedIndex = 0;

    private ArrayList<TextView> items = new ArrayList<>(3);

    private OnItemMenuClickListener itemListener;

    private View.OnClickListener viewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int index = 0; index < items.size(); index++) {
                if (v == items.get(index)) {
                    setSelectedIndex(index);
                    break;
                }
            }
            if (itemListener != null) {
                itemListener.onItemClick(selectedIndex);
            }
        }
    };

    public BookTitlebar(Context context) {
        super(context);
    }

    public BookTitlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookTitlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initItemMenuView();
    }

    /**
     * init the item menu view, must be executed after inflation
     */
    private void initItemMenuView() {
        int cnt = getChildCount();
        for (int i = 0; i < cnt; i++) {
            View v = this.getChildAt(i);
            v.setOnClickListener(viewListener);
            items.add((TextView)v);
        }
        if (items.size() > 0) {
            resetItemsColor();
        }
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < items.size() && index != selectedIndex) {
            this.selectedIndex = index;
            resetItemsColor();
        }
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    private void resetItemsColor() {
        for (TextView v : items) {
            v.setTextColor(Color.parseColor("#aabbcc"));
        }
        items.get(selectedIndex).setTextColor(Color.parseColor("#FF32B6E6"));
    }

    public void setOnItemClickListener(OnItemMenuClickListener listener) {
        this.itemListener = listener;
    }

    public interface OnItemMenuClickListener {
        void onItemClick(int index);
    }

}
