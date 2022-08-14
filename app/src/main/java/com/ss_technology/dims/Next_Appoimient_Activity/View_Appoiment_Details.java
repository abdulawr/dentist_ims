package com.ss_technology.dims.Next_Appoimient_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.ss_technology.dims.Adapter.Appoiment_Container;
import com.ss_technology.dims.Adapter.Lab_Report_Adapter;
import com.ss_technology.dims.Adapter.Lab_Report_Container;
import com.ss_technology.dims.Adapter.Medician_Container;
import com.ss_technology.dims.Adapter.Medicine_Adapter;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Helper.Loading_Dai;
import com.ss_technology.dims.New_Patient_Activity.Treatment_Plan;
import com.ss_technology.dims.R;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class View_Appoiment_Details extends AppCompatActivity {

    DBHelper helper;
    TextView name,mobile,age,address,ref;
    TextView disease,cheif_compalin,extra_oral,sign_symption,diagnose,prev_rec_treat,level_of_satification,intra_oral;
    String appoiment_id;
    Appoiment_Container object;
    RecyclerView medicine_rec,lab_rec;
    ArrayList<Medician_Container> medicine_list;
    ArrayList<Lab_Report_Container> lab_list;
    CardView action_card;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__appoiment__details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         object=(Appoiment_Container) getIntent().getSerializableExtra("obj");
        getSupportActionBar().setTitle(object.getName());
        helper=new DBHelper(this);
        lab_list=new ArrayList<>();
        medicine_list=new ArrayList<>();
        action_card=findViewById(R.id.action_card);
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        age=findViewById(R.id.age);
        address=findViewById(R.id.address);
        ref=findViewById(R.id.ref);
        lab_rec=findViewById(R.id.lab_rec);

        medicine_rec=findViewById(R.id.medicine_rec);
        medicine_rec.setHasFixedSize(true);
        disease=findViewById(R.id.disease);
        intra_oral=findViewById(R.id.intra_oral);
        cheif_compalin=findViewById(R.id.cheif_compalin);
        extra_oral=findViewById(R.id.extra_oral);
        sign_symption=findViewById(R.id.sign_symption);
        diagnose=findViewById(R.id.diagnose);
        prev_rec_treat=findViewById(R.id.prev_rec_treat);
        level_of_satification=findViewById(R.id.level_of_satification);


           /* Patient Record and Data */
            Cursor search=helper.searchPatient(String.valueOf(object.getPatient_id()));
            search.moveToFirst();
            name.setText(search.getString(search.getColumnIndex("name")));
            mobile.setText(search.getString(search.getColumnIndex("mobile")));
            age.setText(String.valueOf(search.getInt(search.getColumnIndex("age"))));
            address.setText(search.getString(search.getColumnIndex("address")));
            ref.setText(search.getString(search.getColumnIndex("ref")));

            /*Medical Report DATA*/
            Cursor medical_report=helper.getMedicalReport(String.valueOf(object.getPatient_id()));
            medical_report.moveToFirst();
            disease.setText(medical_report.getString(medical_report.getColumnIndex("disease")));
            prev_rec_treat.setText(medical_report.getString(medical_report.getColumnIndex("previous_received_treatment")));
            level_of_satification.setText(medical_report.getString(medical_report.getColumnIndex("level_satif")));
            cheif_compalin.setText(medical_report.getString(medical_report.getColumnIndex("cheif_complain")));
            extra_oral.setText(medical_report.getString(medical_report.getColumnIndex("extra_oral")));
            sign_symption.setText(medical_report.getString(medical_report.getColumnIndex("sign_symption")));
            diagnose.setText(medical_report.getString(medical_report.getColumnIndex("diagnose")));
            intra_oral.setText(medical_report.getString(medical_report.getColumnIndex("intra_oral")));

           /* when view pending appoiment */
        if(object.getStatus().trim().equals("pending")){
          appoiment_id=object.getPrev_app_id();
            action_card.setVisibility(View.VISIBLE);
        }
        else{
            appoiment_id=String.valueOf(object.getAppoiment_id());
            action_card.setVisibility(View.GONE);
        }

        /* Medicine Section */
        Cursor medicie_cursor=helper.getMedician(String.valueOf(object.getPatient_id()),appoiment_id);
        int count=medicie_cursor.getCount();
        for (int i=0; i<count; i++){
            medicie_cursor.moveToNext();
            Medician_Container md=new Medician_Container();
            md.setName(medicie_cursor.getString(medicie_cursor.getColumnIndex("name")));
            md.setType(medicie_cursor.getString(medicie_cursor.getColumnIndex("type")));
            md.setDuration(medicie_cursor.getString(medicie_cursor.getColumnIndex("duration")));
            md.setMg(String.valueOf(medicie_cursor.getInt(medicie_cursor.getColumnIndex("mg"))));
            md.setTime_in_day(medicie_cursor.getString(medicie_cursor.getColumnIndex("time")));
            md.setTaking_time(medicie_cursor.getString(medicie_cursor.getColumnIndex("taking_time")));
            medicine_list.add(md);
        }

        if(!medicine_list.isEmpty()){
            Medicine_Adapter ad=new Medicine_Adapter(medicine_list,View_Appoiment_Details.this);
            medicine_rec.setLayoutManager(new LinearLayoutManager(this));
            medicine_rec.setAdapter(ad);
        }

        /* Lab report Section */
        Loading_Dai loading_dai=new Loading_Dai(this);
        Cursor labCursor=helper.getLabReport(String.valueOf(object.getPatient_id()),appoiment_id);
        loading_dai.Show();
        int si=labCursor.getCount();
        for (int i=0; i<si; i++){
            labCursor.moveToNext();
            Lab_Report_Container ll=new Lab_Report_Container();
            String id=String.valueOf(labCursor.getInt(labCursor.getColumnIndex("id")));
            byte[] image=labCursor.getBlob(labCursor.getColumnIndex("image"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            ll.setId(id);
            ll.setBitmap(bitmap);
            lab_list.add(ll);
        }
        if(!lab_list.isEmpty()){
            lab_rec.setHasFixedSize(true);
            lab_rec.setLayoutManager(new LinearLayoutManager(this));
            Lab_Report_Adapter ad=new Lab_Report_Adapter(lab_list, View_Appoiment_Details.this, new Lab_Report_Adapter.OnClick_Card() {
                @Override
                public void getid(String id) {
                    Cursor cur=helper.getLabReport(id);
                    cur.moveToFirst();
                    byte[] img=cur.getBlob(cur.getColumnIndex("image"));
                    Bitmap bi=BitmapFactory.decodeByteArray(img,0,img.length);
                    View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_image_full_screen,null);
                    AlertDialog.Builder al=new AlertDialog.Builder(View_Appoiment_Details.this);
                    al.setView(view);
                    Dialog dialog=al.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    ImageView close_image=view.findViewById(R.id.close_icon);
                    close_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    PhotoView full_image=view.findViewById(R.id.full_image);
                    full_image.setImageBitmap(bi);
                    dialog.show();
                }
            });
            lab_rec.setAdapter(ad);
        }
        loading_dai.Hide();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Medicine_For_This_appoiment(View view) {
        Loading_Dai loading_dai=new Loading_Dai(View_Appoiment_Details.this);
        loading_dai.Show();
        if (helper.updateAppoimentStatus(String.valueOf(object.getAppoiment_id()))){
            Intent intent=new Intent(getApplicationContext(), Treatment_Plan.class);
            intent.putExtra("patient_id",String.valueOf(object.getPatient_id()));
            intent.putExtra("appoiment_id",String.valueOf(object.getAppoiment_id()));
            startActivity(intent);
            finish();
        }
        else{
            Toasty.warning(View_Appoiment_Details.this,"Can`t update the status of appoiment",Toasty.LENGTH_LONG).show();
        }
        loading_dai.Hide();
    }

    public void Complete_The_appoiment(View view) {
        Loading_Dai loading_dai=new Loading_Dai(View_Appoiment_Details.this);
        loading_dai.Show();
        if (helper.updateAppoimentStatus(String.valueOf(object.getAppoiment_id()))){
            Toasty.success(View_Appoiment_Details.this,"Status changed successfully!",Toasty.LENGTH_LONG).show();
        }
        else{
            Toasty.warning(View_Appoiment_Details.this,"Can`t update the status of appoiment",Toasty.LENGTH_LONG).show();
        }
        loading_dai.Hide();
    }
}