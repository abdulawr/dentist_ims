package com.ss_technology.dims.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ss_technology.dims.R;
import com.ss_technology.dims.SharePref.DoctorData;
import com.ss_technology.dims.SharePref.LoginPref;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {

    DoctorData data;
    LoginPref loginPref;
    TextInputEditText id,pass;
    boolean ch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        data=new DoctorData(this);
        loginPref=new LoginPref(this);
        id=findViewById(R.id.id);
        pass=findViewById(R.id.pass);


    }

    public void LogIn(View view) {
        String idVal=id.getText().toString(),passVal=pass.getText().toString();
        if(validate(idVal) && validate(passVal))
        {
            String[] result=data.get();
            if (idVal.trim().equals(result[0].trim()) && passVal.trim().equals(result[2].trim()))
            {
                loginPref.set(true);
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }
            else{
                Toasty.warning(this,"Invalid ID or Password",Toast.LENGTH_LONG,true).show();
            }
        }
        else {
            Toasty.error(this, "Fill the form correctly.", Toast.LENGTH_SHORT, true).show();
        }
    }

    private boolean validate(String val)
    {
        return (val.trim().length() > 0) ? true : false;
    }


    public void ForgotPassword(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View alertview= getLayoutInflater().inflate(R.layout.forgot_password_view, null);
        builder.setView(alertview);
        builder.setCancelable(false);
        Dialog dialog=builder.create();
        dialog.show();

        ImageButton btn=alertview.findViewById(R.id.close);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextInputEditText mob=alertview.findViewById(R.id.mob);
        TextInputEditText newPass=alertview.findViewById(R.id.pass);
        Button button=alertview.findViewById(R.id.save);
        TextInputLayout passTextLayout=alertview.findViewById(R.id.passTextLayout);
        TextInputLayout textinputMobile=alertview.findViewById(R.id.textinputMobile);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ch == false)
                {
                    String[] result=data.get();
                    String mobResult=mob.getText().toString();
                    if(mobResult.trim().length() > 0)
                    {
                       if(mobResult.trim().equals(result[1].trim()))
                       {
                          textinputMobile.setVisibility(View.GONE);
                           passTextLayout.setVisibility(View.VISIBLE);
                           ch=true;
                       }
                       else {
                           Toasty.error(Login.this,"Invalid Mobile Number",Toast.LENGTH_LONG,true).show();
                       }
                    }
                    else{
                        Toasty.warning(Login.this,"Enter Value First",Toast.LENGTH_LONG,true).show();
                    }
                }

                else {
                    String newPassVal=newPass.getText().toString();
                    if(!TextUtils.isEmpty(newPassVal))
                    {
                        data.setPass(newPassVal);
                        dialog.dismiss();
                        ch=false;
                        Toasty.success(Login.this,"Successfully changed!",Toast.LENGTH_LONG,true).show();
                    }
                    else {
                        Toasty.warning(Login.this,"Enter Value First",Toast.LENGTH_LONG,true).show();
                    }
                }

            }
        });
    }
}