package com.haoye.dartreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @brief ContentPagerAdapter
 * @detail
 * @see FragmentPagerAdapter
 * @author Haoye
 * @date 2016/4/30
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public ContentPagerAdapter(FragmentManager manager, ArrayList<Fragment> list) {
        super(manager);
        this.fragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}