package com.haoye.dartreader;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author Haoye
 * @brief
 * @detail
 * @date 2017-03-21
 * @see
 */
public class ContentControlBar implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private Activity activity;
    private int interval = 1;

    private String replaceString = "�";
    private String originalText = null;

    private int clickedSpinnerId = 0;

    private EditText maskEditTxt;
    private Button   updateBtn;
    private Spinner textSizeOption;
    private Spinner fgColorOption;
    private Spinner bgColorOption;


    private OnSettingsChangedListener settingsChangedListener;

    public ContentControlBar(Activity activity) {
        this.activity = activity;
        init();
    }

    public void setSettingsChangedListener(OnSettingsChangedListener settingsChangedListener) {
        this.settingsChangedListener = settingsChangedListener;
    }

    private void init() {
        findViews();
        initUpdateButton();
        initSpinners();
    }

    private void findViews() {
        maskEditTxt    = (EditText) activity.findViewById(R.id.replaceEditText);
        updateBtn      = (Button) activity.findViewById(R.id.updateBtn);
        textSizeOption = (Spinner) activity.findViewById(R.id.textSizeOption);
        fgColorOption  = (Spinner) activity.findViewById(R.id.fgColorOption);
        bgColorOption  = (Spinner) activity.findViewById(R.id.bgColorOption);
    }

    private void initUpdateButton() {
        updateBtn.setOnClickListener(this);
    }

    private void initSpinners() {
        textSizeOption.setOnItemSelectedListener(this);
        fgColorOption.setOnItemSelectedListener(this);
        bgColorOption.setOnItemSelectedListener(this);
    }

    public void setVisibility(int visibility) {
        activity.findViewById(R.id.contentControlBar).setVisibility(visibility);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updateBtn:
                onUpdateBtnClick();
                break;

        }
    }

    private void onUpdateBtnClick() {
        if (settingsChangedListener != null) {
            settingsChangedListener.onMaskCharsChange(maskEditTxt.getText().toString(), interval);
        }
    }

    private void onTextSizeOptionSelected(int which) {
        // TODO: 2017-03-21 change
        if (settingsChangedListener != null) {
            settingsChangedListener.onTextSizeChange(18);
        }
    }

    private void onFgColorOptionSelected(int which) {
        // TODO: 2017-03-21 change
        if (settingsChangedListener != null) {
            settingsChangedListener.onForegroundColorChange(Color.parseColor("#ffffff"));
        }
    }

    private void onBgColorOptionSelected(int which) {
        // TODO: 2017-03-21
        if (settingsChangedListener != null) {
            settingsChangedListener.onBackgroundColorChange(Color.parseColor("#000000"));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (clickedSpinnerId) {
            case R.id.textSizeOption:
                onTextSizeOptionSelected(position);
                break;
            case R.id.fgColorOption:
                onFgColorOptionSelected(position);
                break;
            case R.id.bgColorOption:
                onBgColorOptionSelected(position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnSettingsChangedListener {

        void onMaskCharsChange(String mask, int interval);

        void onIntervalChange(String mask, int interval);

        void onTextSizeChange(int newSize);

        void onForegroundColorChange(int newColor);

        void onBackgroundColorChange(int newColor);

    }

}
