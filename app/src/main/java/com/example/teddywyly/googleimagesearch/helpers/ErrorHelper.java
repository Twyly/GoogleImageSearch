package com.example.teddywyly.googleimagesearch.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.teddywyly.googleimagesearch.R;

/**
 * Created by teddywyly on 5/16/15.
 */
public class ErrorHelper {

    public enum ErrorType {
        NOMOREDATA,
        GENERIC,
        NETWORK;
    }

    public static void showErrorAlert(Context context, ErrorType error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = null;
        switch (error) {
            case NETWORK:
                message = context.getString(R.string.network_failure_warning);
                break;
            case GENERIC:
                message = context.getString(R.string.general_failure_warning);
                break;
            case NOMOREDATA:
                message = context.getString(R.string.no_more_data_failure_warning);
                break;
        }
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.casual_dialog_accept,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.create().show();
    }
}
