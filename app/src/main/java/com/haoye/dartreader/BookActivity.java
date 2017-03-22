package com.haoye.dartreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.haoye.dartreader.book.BookTitlebar;
import com.haoye.dartreader.book.ContentFragment;
import com.haoye.dartreader.book.OpenedBookManager;

import java.util.ArrayList;


public class BookActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        viewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void initAutoHide() {
        mVisible = true;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        };
        originalFragment.setOnContentClickListener(listener);
        mode1Fragment.setOnContentClickListener(listener);
        mode2Fragment.setOnContentClickListener(listener);
    }

//================================================================================
    private BookTitlebar      titlebar;
    private ViewPager         viewPager;

    private ContentControlBar controlBar;
    private ContentFragment originalFragment = ContentFragment.create();
    private ContentFragment mode1Fragment    = ContentFragment.create();

    private ContentFragment mode2Fragment    = ContentFragment.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        init();
    }

    private void findViews() {
        titlebar = (BookTitlebar) findViewById(R.id.bookTitlebar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
    }

    private void init() {
        findViews();
        initTitlebar();
        initViewPager();
        initControlBar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initFragmentText();
                initAutoHide();
            }
        }, 1000);
    }

    private void initFragmentText() {
        String original = OpenedBookManager.getOriginalText();
        originalFragment.setText(original);
        String mode1 = OpenedBookManager.getPatternString("�", 0, 1);
        mode1Fragment.setText(mode1);
        String mode2 = OpenedBookManager.getPatternString("�", 1, 1);
        mode2Fragment.setText(mode2);
    }

    private void initTitlebar() {
        titlebar.setOnItemClickListener(new BookTitlebar.OnItemMenuClickListener() {
            @Override
            public void onItemClick(int index) {
                if (index >= 0 && index < viewPager.getChildCount()) {
                    viewPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(originalFragment);
        fragments.add(mode1Fragment);
        fragments.add(mode2Fragment);
        viewPager.setAdapter(new ContentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                hideSoftInputForm();
                titlebar.setSelectedIndex(position);
                if (position == 0) {
                    controlBar.setVisibility(View.GONE);
                }
                else {
                    controlBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideSoftInputForm();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                hideSoftInputForm();
            }
        });
    }

    private void initControlBar() {
        controlBar = new ContentControlBar(this);
        controlBar.setSettingsChangedListener(new ContentControlBar.OnSettingsChangedListener() {
            @Override
            public void onMaskCharsChange(String mask, int interval) {
                String mode1 = OpenedBookManager.getPatternString(mask, 0, interval);
                mode1Fragment.setText(mode1);
                String mode2 = OpenedBookManager.getPatternString(mask, 1, interval);
                mode2Fragment.setText(mode2);
            }

            @Override
            public void onIntervalChange(String mask, int interval) {
                String mode1 = OpenedBookManager.getPatternString(mask, 0, interval);
                mode1Fragment.setText(mode1);
                String mode2 = OpenedBookManager.getPatternString(mask, 1, interval);
                mode2Fragment.setText(mode2);
            }

            @Override
            public void onTextSizeChange(int newSize) {
                // TODO: 2017-03-22
            }

            @Override
            public void onForegroundColorChange(int newColor) {
                // TODO: 2017-03-22

            }

            @Override
            public void onBackgroundColorChange(int newColor) {
                // TODO: 2017-03-22

            }
        });
    }

    private void hideSoftInputForm() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
