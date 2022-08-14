package com.ss_technology.dims.Services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Helper.HelperFunctions;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SendingAppoimentMessage extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
        Toasty.success(getApplicationContext(),"Messages sended successfully",Toasty.LENGTH_LONG).show();
        stopService(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
