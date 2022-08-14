package com.ss_technology.dims.Helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.ss_technology.dims.R;

public class Loading_Dai {

    Context context;
    AlertDialog.Builder alertdialoge;
    Dialog dialog;

    public Loading_Dai(Context context)
    {
        this.context=context;
    }

    public void Show()
    {
        try
        {
            View view= LayoutInflater.from(context).inflate(R.layout.loading,null);
            alertdialoge=new AlertDialog.Builder(context)
                    .setCancelable(false)
                    .setView(view);
            dialog=alertdialoge.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }

    }

    public void Hide()
    {
        try
        {
            if(alertdialoge != null)
                dialog.dismiss();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
