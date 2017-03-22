package com.haoye.dartreader.book;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoye.dartreader.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {
    private View     rootView;
    private TextView contentTxtV;

    public static ContentFragment create() {
        return new ContentFragment();
    }

    public ContentFragment() {
        // Required empty public constructor
    }

    public void setText(String content) {
        this.contentTxtV.setText(content);
    }

    public void setOnContentClickListener(View.OnClickListener listener) {
        this.contentTxtV.setOnClickListener(listener);
    }

    public void appendText(String text) {
        this.contentTxtV.append(text);
    }

    public void setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        contentTxtV.setTextColor(color);
    }

    public void setTextSize(int size) {
        contentTxtV.setTextSize(size);
    }

    private void findViews() {
        rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_content, null);
        contentTxtV = (TextView) rootView.findViewById(R.id.contentTxtV);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeAllViewsInLayout();
        }
    }

}
