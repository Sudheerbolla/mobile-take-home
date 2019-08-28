package com.guestlogix.views.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.guestlogix.R;
import com.guestlogix.utils.StaticUtils;
import com.guestlogix.views.fragments.BaseFragment;
import com.guestlogix.views.fragments.EpisodesFragment;
import com.guestlogix.utils.Constants;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relHeader;
    private AppCompatTextView txtHeading;
    private AppCompatImageView imgback;
    private View viewSeperator;
    public FragmentManager fragmentManager;
    private long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    void initComponents() {
        fragmentManager = getSupportFragmentManager();
        setReferences();
        setListeners();
        replaceFragmentWithoutAnimation(EpisodesFragment.newInstance(), false);
    }

    private void setListeners() {
        imgback.setOnClickListener(this);
    }

    public void showEmptyTopBar() {
        relHeader.setVisibility(View.INVISIBLE);
        viewSeperator.setVisibility(View.INVISIBLE);
    }

    public void showTopBar() {
        showTopBar("", false);
    }

    public void showTopBar(String heading, boolean showBackButton) {
        relHeader.setVisibility(View.VISIBLE);
        viewSeperator.setVisibility(View.VISIBLE);
        imgback.setVisibility(showBackButton ? View.VISIBLE : View.GONE);
        txtHeading.setText(!TextUtils.isEmpty(heading) ? heading : getString(R.string.app_name));
    }

    public void hideTopBar() {
        relHeader.setVisibility(View.GONE);
        viewSeperator.setVisibility(View.GONE);
    }

    private void setReferences() {
        relHeader = findViewById(R.id.relHeader);
        txtHeading = findViewById(R.id.txtHeading);
        imgback = findViewById(R.id.imgback);
        viewSeperator = findViewById(R.id.viewSeperator);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgback) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();
        if (fragment instanceof EpisodesFragment) {
            if (backPressed + Constants.BACK_PRESSED_TIME > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                StaticUtils.showToast(this, getString(R.string.press_once_again_to_exit));
            }
            backPressed = System.currentTimeMillis();
        } else super.onBackPressed();
    }

    private BaseFragment getCurrentFragment() {
        return (BaseFragment) fragmentManager.findFragmentById(R.id.mainFrame);
    }

}
