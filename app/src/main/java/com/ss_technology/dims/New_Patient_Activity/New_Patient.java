package com.ss_technology.dims.New_Patient_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.R;

import es.dmoral.toasty.Toasty;

public class New_Patient extends AppCompatActivity {

    TextInputEditText name,mobile,address,age,ref;
    // ref is optional
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__patient);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        helper=new DBHelper(New_Patient.this);

    }

    private void init(){
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        address=findViewById(R.id.address);
        age=findViewById(R.id.age);
        ref=findViewById(R.id.ref);
    }

    public void SubmitButtonClick(View view) {
       if(check(name) && check(mobile) && check(address) && check(age)){
       if (helper.insertPatientDetails(name.getText().toString(),mobile.getText().toString(),address.getText().toString(),
               age.getText().toString(),ref.getText().toString())){
          Intent intent=new Intent(getApplicationContext(),PatientDiseaseDetails.class);
          intent.putExtra("id",String.valueOf(helper.getLastAddedRowId()));
          startActivity(intent);
          finish();
       }
       else{
           Toasty.warning(getApplicationContext(),"Patient record can`t be created try later!").show();
       }
       }
       else{
           Toasty.error(getApplicationContext(),"Fill the values correctly").show();
       }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();
        return  true;
    }

    private boolean check(TextInputEditText edit){
       String value=edit.getText().toString();
       if(value.trim().length() > 0 && !value.equals("")){
          return true;
       }
       else{
           return false;
       }
    }
}