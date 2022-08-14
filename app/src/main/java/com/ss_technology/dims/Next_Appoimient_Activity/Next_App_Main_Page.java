package com.ss_technology.dims.Next_Appoimient_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ss_technology.dims.Adapter.Appoiment_Adapter;
import com.ss_technology.dims.Adapter.Appoiment_Container;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.New_Patient_Activity.PatientDiseaseDetails;
import com.ss_technology.dims.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Next_App_Main_Page extends AppCompatActivity {

    EditText doc_query;
    DBHelper helper;
    TextView error;
    CardView patient_infor_layout;
    TextView name,mobile,age,address,app;
    RecyclerView recyclerView;
    Button create_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next__app__main__page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        doc_query=findViewById(R.id.doc_query);
        helper=new DBHelper(Next_App_Main_Page.this);
        error=findViewById(R.id.error);
        patient_infor_layout=findViewById(R.id.patient_infor_layout);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        age=findViewById(R.id.age);
        address=findViewById(R.id.address);
        recyclerView=findViewById(R.id.rec);
        app=findViewById(R.id.app);
        create_app=findViewById(R.id.create_app);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void OnSearchButtonClick(View view) {
        String query=doc_query.getText().toString();
        if(query.trim().length() > 0){
            Cursor search=helper.searchPatient(query);
            search.moveToFirst();
            if(search.getCount() > 0){
              error.setVisibility(View.GONE);
              doc_query.getText().clear();
              patient_infor_layout.setVisibility(View.VISIBLE);
              recyclerView.setVisibility(View.VISIBLE);
              String id=String.valueOf(search.getInt(search.getColumnIndex("id")));
              name.setText(search.getString(search.getColumnIndex("name")));
              mobile.setText(search.getString(search.getColumnIndex("mobile")));
              age.setText(String.valueOf(search.getInt(search.getColumnIndex("age"))));
              address.setText(search.getString(search.getColumnIndex("address")));
              String patient_name=search.getString(search.getColumnIndex("name"));

                // create appoiment if patient does not have any appoinment || or patient does not have any pending appioment
                create_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(), PatientDiseaseDetails.class);
                        intent.putExtra("id",id);
                        startActivity(intent);
                        finish();
                    }
                });

                ArrayList<Appoiment_Container> list=new ArrayList<>();
                Cursor appoiment=helper.searchAppoiment(id);
                Cursor prev_id_checker=helper.searchAppoiment(id);
                int size = appoiment.getCount();
                String prev_app_id="";
                boolean check_pending_app=helper.getAppoimentDetailsWithStatus(id);
                if(size > 0)
                {
                    if (check_pending_app){
                        prev_id_checker.moveToPosition(1);
                        prev_app_id=String.valueOf(prev_id_checker.getInt(prev_id_checker.getColumnIndex("id")));
                    }
                    else{
                        prev_id_checker.moveToPosition(0);
                        prev_app_id=String.valueOf(prev_id_checker.getInt(prev_id_checker.getColumnIndex("id")));
                    }
                }

                for (int i = 0; i < size; i++) {
                    appoiment.moveToNext();
                    Appoiment_Container ap=new Appoiment_Container();
                    ap.setPatient_id(Integer.parseInt(id));
                    ap.setAppoiment_id(appoiment.getInt(appoiment.getColumnIndex("id")));
                    ap.setDate(appoiment.getString(appoiment.getColumnIndex("date")));
                    ap.setStatus(appoiment.getString(appoiment.getColumnIndex("status")));
                    ap.setName(patient_name);
                    ap.setPrev_app_id(prev_app_id);
                    list.add(ap);
                }
                if (!list.isEmpty()){
                    app.setVisibility(View.VISIBLE);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    Appoiment_Adapter adapter=new Appoiment_Adapter(list,Next_App_Main_Page.this);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    app.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                    error.setText("No appointments");
                }
            }
            else{
                error.setText("Record Not Found");
               error.setVisibility(View.VISIBLE);
               patient_infor_layout.setVisibility(View.GONE);
               recyclerView.setVisibility(View.GONE);
               app.setVisibility(View.GONE);
                create_app.setVisibility(View.GONE);
            }

        }
        else{
            Toasty.warning(getApplicationContext(),"Enter query first",Toasty.LENGTH_LONG).show();
        }
    }
}