package com.guestlogix.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.guestlogix.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StaticUtils {

    public static boolean sDisableFragmentAnimations = true;

    public static final String COURSE_DISPLAY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static boolean checkInternetConnection(Context context) {
        NetworkInfo _activeNetwork = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return _activeNetwork != null && _activeNetwork.isConnectedOrConnecting();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showSnackBarToast(View container, String message) {
        try {
            Snackbar snackbar = snackbar(container, message, Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSnackBarToast(View container, String message, String actionText, View.OnClickListener onClickListener) {
        try {
            Snackbar snackbar = snackbar(container, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(actionText, onClickListener);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showIndefiniteToast(View container, String message, String actionText, View.OnClickListener onClickListener) {
        try {
            Snackbar snackbar = snackbar(container, message, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(actionText, onClickListener);
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Snackbar snackbar(View container, String message, int duration) {
        Snackbar snackbar = Snackbar.make(container, message, duration);
        try {
            View sbView = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) sbView.getLayoutParams();
            params.gravity = (Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//            params.setMargins(0, 0, 0, Math.round(softKeyBarHeight));
            sbView.setLayoutParams(params);
            sbView.setBackgroundColor(container.getResources().getColor(R.color.colorWhite));
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.BLACK);
            textView.setMaxLines(3);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
//            snackbar.setActionTextColor(Color.RED);
//            snackbar.setActionTextColor(sbView.getContext().getResources().getColor(R.color.color_red));
//            TextView action = sbView.findViewById(R.id.snackbar_action);
//            action.setTextColor(sbView.getContext().getResources().getColor(R.color.color_red));
            snackbar.addCallback(new Snackbar.Callback() {

                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                        // Snackbar closed on its own
                    }
                    super.onDismissed(transientBottomBar, event);
                }

                @Override
                public void onShown(Snackbar sb) {
                    super.onShown(sb);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return snackbar;
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity act) {
        try {
            if (act.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception ignored) {
        }
    }

    public static String getDays(String GivenTime) {
        Calendar calendarGiven = Calendar.getInstance();
        Calendar calendarCurrent = Calendar.getInstance();
        try {
            calendarGiven.setTime(new SimpleDateFormat(COURSE_DISPLAY_DATE_FORMAT, Locale.getDefault()).parse(GivenTime));

            int yearsInBetween = calendarCurrent.get(Calendar.YEAR) - calendarGiven.get(Calendar.YEAR);
            int monthsDiff = calendarCurrent.get(Calendar.MONTH) - calendarGiven.get(Calendar.MONTH);
            int daysDiff = calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendarGiven.get(Calendar.DAY_OF_MONTH);
            if (yearsInBetween > 1) {
                return yearsInBetween + " years ago";
            } else if (monthsDiff > 1) {
                return monthsDiff + " months ago";
            } else if (daysDiff > 1) {
                return daysDiff + " days ago";
            } else return "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
