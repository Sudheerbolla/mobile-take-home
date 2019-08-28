package com.guestlogix.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.guestlogix.R;
import com.guestlogix.utils.StaticUtils;

public abstract class BaseActivity extends AppCompatActivity {

    abstract void initComponents();

    public void replaceFragment(Fragment fragment, boolean needToAddToBackStack) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(fragmentTransaction, false);
        if (needToAddToBackStack)
            fragmentTransaction.replace(R.id.mainFrame, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        else
            fragmentTransaction.replace(R.id.mainFrame, fragment, tag).commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, boolean needToAddToBackStack) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(fragmentTransaction, false);
        if (needToAddToBackStack)
            fragmentTransaction.add(R.id.mainFrame, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        else
            fragmentTransaction.add(R.id.mainFrame, fragment, tag).commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment, boolean needToAddToBackStack, int containerId) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(fragmentTransaction, false);
        if (needToAddToBackStack)
            fragmentTransaction.replace(containerId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        else
            fragmentTransaction.replace(containerId, fragment, tag).commitAllowingStateLoss();
    }

    public void addFragment(Fragment fragment, boolean needToAddToBackStack, int containerId) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        setCustomAnimation(fragmentTransaction, false);
        if (needToAddToBackStack)
            fragmentTransaction.add(containerId, fragment, tag).addToBackStack(tag).commit();
        else
            fragmentTransaction.add(containerId, fragment, tag).commit();
    }

    public void replaceFragmentWithoutAnimation(Fragment fragment, boolean needToAdd) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        StaticUtils.sDisableFragmentAnimations = true;
        if (needToAdd)
            fragmentTransaction.replace(R.id.mainFrame, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        else
            fragmentTransaction.replace(R.id.mainFrame, fragment, tag).commitAllowingStateLoss();
        StaticUtils.sDisableFragmentAnimations = false;
    }

    public void replaceFragmentWithoutAnimation(Fragment fragment, int containerId, boolean needToAdd) {
        StaticUtils.hideSoftKeyboard(this);
        String tag = fragment.getClass().getSimpleName();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        StaticUtils.sDisableFragmentAnimations = true;
        if (needToAdd)
            fragmentTransaction.replace(containerId, fragment, tag).addToBackStack(tag).commitAllowingStateLoss();
        else
            fragmentTransaction.replace(containerId, fragment, tag).commitAllowingStateLoss();
        StaticUtils.sDisableFragmentAnimations = false;
    }

    public void clearBackStack() {
        FragmentManager fragment = getSupportFragmentManager();
        fragment.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void clearBackStackCompletely() {
        StaticUtils.sDisableFragmentAnimations = true;
        FragmentManager fragment = getSupportFragmentManager();
        fragment.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        StaticUtils.sDisableFragmentAnimations = false;
    }

    public void popBackStack() {
        StaticUtils.hideSoftKeyboard(this);
        FragmentManager fragment = getSupportFragmentManager();
        setCustomAnimation(fragment.beginTransaction(), true);
        fragment.popBackStackImmediate();
    }

    private static void setCustomAnimation(FragmentTransaction ft, boolean reverseAnimation) {
        if (!reverseAnimation) {
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }

}