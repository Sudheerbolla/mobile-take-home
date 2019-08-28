package com.guestlogix.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.guestlogix.R;

public class DialogUtils {

    public static void showTwoButtonDialog(Context context, String message, String positiveText, final View.OnClickListener positiveOnClickListener, String negativeText, final View.OnClickListener negativeOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (positiveOnClickListener != null) positiveOnClickListener.onClick(null);
            }
        });
        builder.setNeutralButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (negativeOnClickListener != null) negativeOnClickListener.onClick(null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

}
