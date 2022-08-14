package com.ss_technology.dims.SharePref;

import android.content.Context;
import android.content.SharedPreferences;

import com.ss_technology.dims.Activities.Login;

public class LoginPref {
    SharedPreferences sharedPreferences;
    Context context;
    public LoginPref(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("login",Context.MODE_PRIVATE);
    }

    public void set(boolean val)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("check",val);
        editor.apply();
    }

    public boolean get()
    {
        return sharedPreferences.getBoolean("check",false);
    }
}
