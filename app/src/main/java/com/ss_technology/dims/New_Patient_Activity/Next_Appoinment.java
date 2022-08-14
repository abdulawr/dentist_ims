package com.ss_technology.dims.New_Patient_Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Next_Appoinment extends AppCompatActivity {

    String patient_id,appoimnet_id;
    DatePicker datepicker;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next__appoinment);

        patient_id=getIntent().getStringExtra("patient_id");
        appoimnet_id=getIntent().getStringExtra("appoiment_id");

        datepicker=findViewById(R.id.datepicker);
        helper=new DBHelper(this);

    }

    public void set_Appoimnet(View view) {
        Date d=new Date();
        d.setDate(datepicker.getDayOfMonth());
        d.setMonth(datepicker.getMonth());
        d.setYear(datepicker.getYear()-1900);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(d);

        if (helper.createAppoiment(patient_id,"pending",formattedDate)){
            Intent intent=new Intent(getApplicationContext(),PrintDetails.class);
            intent.putExtra("patient_id",patient_id);
            intent.putExtra("appoiment_id",appoimnet_id);
            startActivity(intent);
            finish();
        }
        else{
            Toasty.error(getApplicationContext(),"Something went wrong try again").show();
        }
    }

    public void Skip(View view) {
        Intent intent=new Intent(getApplicationContext(),PrintDetails.class);
        intent.putExtra("patient_id",patient_id);
        intent.putExtra("appoiment_id",appoimnet_id);
        startActivity(intent);
        finish();
    }
}