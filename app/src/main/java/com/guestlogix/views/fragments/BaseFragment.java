package com.guestlogix.views.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public abstract class BaseFragment extends Fragment {

    public static FragmentManager childFragmentManager;

    protected abstract void initComponents();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childFragmentManager = getChildFragmentManager();
    }

    public int checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }

    public void requestForPermission(String permission, int requestCode) {
        requestPermissions(new String[]{permission}, requestCode);
    }

    public void requestForPermission(String[] permission, int requestCode) {
        requestPermissions(permission, requestCode);
    }

}
