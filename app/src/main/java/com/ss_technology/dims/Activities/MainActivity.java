package com.ss_technology.dims.Activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Fragment.AddNewDisease;
import com.ss_technology.dims.Helper.HelperFunctions;
import com.ss_technology.dims.Helper.Loading_Dai;
import com.ss_technology.dims.New_Patient_Activity.New_Patient;
import com.ss_technology.dims.Next_Appoimient_Activity.Next_App_Main_Page;
import com.ss_technology.dims.R;
import com.ss_technology.dims.Services.AlarmReceiver;
import com.ss_technology.dims.Services.SendingAppoimentMessage;
import com.ss_technology.dims.SharePref.LoginPref;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LoginPref loginPref;
    DrawerLayout drawer;
    DBHelper helper;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginPref=new LoginPref(this);

         drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        drawer.addDrawerListener(toggle);
        helper=new DBHelper(this);

        if(!helper.checkDiseaseList()){
            helper.setInitialDisease();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {} else {if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
        }}

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            alarmMgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

            SharedPreferences prefss = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefss.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

    }

    public void New_Patient_Click(View view)
    {
      startActivity(new Intent(getApplicationContext(), New_Patient.class));
    }

    public void Next_Appointment_click(View view)
    {
        startActivity(new Intent(getApplicationContext(), Next_App_Main_Page.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signout)
        {
            loginPref.set(false);
            startActivity(new Intent(this,Login.class));
            finish();
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if (id == R.id.addDisease)
        {
            AddNewDisease addNewDisease=new AddNewDisease(MainActivity.this);
            addNewDisease.show(getSupportFragmentManager(),"Show");
            addNewDisease.setCancelable(false);
        }
        else if(id == R.id.sendMsg)
        {
            Loading_Dai loading_dai=new Loading_Dai(MainActivity.this);
            loading_dai.Show();
            DBHelper helper=new DBHelper(getApplicationContext());
            String date= HelperFunctions.currentDate();
            String[] dd=date.split("/");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            Date tomorrow = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            String next3Datefromcurrent = dateFormat.format(tomorrow);
            String searchDate=next3Datefromcurrent+"/"+dd[1]+"/"+dd[2];
            Cursor cursor=helper.getSMSappoimentList(searchDate);
            int ss=cursor.getCount();
            for (int i=0; i<ss; i++){
                cursor.moveToNext();

                String pat_id=String.valueOf(cursor.getInt(cursor.getColumnIndex("patient_id")));
                Cursor patient=helper.getPatientDetails(pat_id);
                patient.moveToFirst();

                String message="Hello Dear "+patient.getString(patient.getColumnIndex("name"))+
                        ", this is a reminder message of your upcoming appointment with DR. Anwar Ali Khan on "+
                        cursor.getString(cursor.getColumnIndex("date"))+" Thank you!";
                try
                {
                    SmsManager smsMgrVar = SmsManager.getDefault();
                    smsMgrVar.sendTextMessage(patient.getString(patient.getColumnIndex("mobile")), null, message, null, null);
                    helper.updateAppoimentMessageStatus(String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))),1);
                }
                catch (Exception exception)
                {
                    Log.e("Basit",exception.getMessage());
                }
            }
            loading_dai.Hide();
            Toasty.success(getApplicationContext(),"Messages sended successfully",Toasty.LENGTH_LONG).show();
        }

       drawer.closeDrawer(Gravity.LEFT);
        return true;
    }
}