package com.ss_technology.dims.SharePref;

import android.content.Context;
import android.content.SharedPreferences;

public class SignUpPref {

    SharedPreferences sharedPreferences;
    Context context;
    public SignUpPref(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("signUp",Context.MODE_PRIVATE);
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
