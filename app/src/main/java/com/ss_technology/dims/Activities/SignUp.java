package com.ss_technology.dims.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.dims.R;
import com.ss_technology.dims.SharePref.DoctorData;
import com.ss_technology.dims.SharePref.SignUpPref;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity {

    TextInputEditText mobile,id,developer,password,name;
    DoctorData doctorData;
    SignUpPref signUpPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        doctorData=new DoctorData(this);
        signUpPref=new SignUpPref(this);

        mobile=findViewById(R.id.mobile);
        id=findViewById(R.id.id);
        developer=findViewById(R.id.developer);
        password=findViewById(R.id.pass);
        name=findViewById(R.id.name);

    }


    public void SignUp(View view) {
        String idVal=id.getText().toString();
        String MobileVal=mobile.getText().toString();
        String PassVal=password.getText().toString();
        String DeveloperVal=developer.getText().toString();
        String nameVal=name.getText().toString();

        if(validate(idVal) && validate(MobileVal) && validate(PassVal) & validate(DeveloperVal) && validate(nameVal))
        {
           if(DeveloperVal.trim().equals("03059235079")){
               doctorData.set(idVal,MobileVal,PassVal,nameVal);
               signUpPref.set(true);
               startActivity(new Intent(this,Login.class));
               finish();
               Toasty.success(this,"Account created successfully!",Toast.LENGTH_LONG,true).show();
           }
           else {
               Toasty.warning(this,"Invalid Developer Key (Ask from developer)",Toast.LENGTH_LONG,true).show();
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
}