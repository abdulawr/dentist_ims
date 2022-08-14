package com.ss_technology.dims.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.ss_technology.dims.New_Patient_Activity.Next_Appoinment;
import com.ss_technology.dims.New_Patient_Activity.PatientDiseaseDetails;
import com.ss_technology.dims.New_Patient_Activity.PrintDetails;
import com.ss_technology.dims.New_Patient_Activity.Treatment_Plan;
import com.ss_technology.dims.R;
import com.ss_technology.dims.SharePref.LoginPref;
import com.ss_technology.dims.SharePref.SignUpPref;

public class SplashScreen extends AppCompatActivity {

    LoginPref loginPref;
    SignUpPref signUpPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        loginPref=new LoginPref(this);
        signUpPref=new SignUpPref(this);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(signUpPref.get())
                {
                    if(loginPref.get())
                    {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                }
                else {
                    startActivity(new Intent(getApplicationContext(), SignUp.class));
                    finish();
                }
            }
        },1000);
    }
}