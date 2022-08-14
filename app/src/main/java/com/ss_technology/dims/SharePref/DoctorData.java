package com.ss_technology.dims.SharePref;

import android.content.Context;
import android.content.SharedPreferences;

public class DoctorData {

    SharedPreferences sharedPreferences;
    Context context;

    public DoctorData(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("doctorData",Context.MODE_PRIVATE);
    }

    public void set(String id,String mobile,String pass,String name)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("mobile",mobile);
        editor.putString("pass",pass);
        editor.putString("name",name);
        editor.apply();
    }

    public void setPass(String pass)
    {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("pass",pass);
        editor.apply();
    }

    public String[] get()
    {
        return new String[]{sharedPreferences.getString("id","null"),sharedPreferences.getString("mobile","null"),sharedPreferences.getString("pass","null"),sharedPreferences.getString("name","null")};
    }

}
