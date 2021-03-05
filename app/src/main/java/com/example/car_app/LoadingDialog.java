package com.example.car_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }
    void startLoadingDialog(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            LayoutInflater inflater = activity.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.loading_dialog, null));
            builder.setCancelable(true);

            alertDialog = builder.create();
            try {
                alertDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void dismissDialog(){
        alertDialog.dismiss();
    }
}
