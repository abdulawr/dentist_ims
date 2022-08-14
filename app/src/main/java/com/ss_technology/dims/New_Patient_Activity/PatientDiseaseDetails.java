package com.ss_technology.dims.New_Patient_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ss_technology.dims.Activities.MainActivity;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Helper.HelperFunctions;
import com.ss_technology.dims.Helper.Loading_Dai;
import com.ss_technology.dims.R;

import java.io.InputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PatientDiseaseDetails extends AppCompatActivity {

    DBHelper helper;
    LinearLayout topLinearLayout;
    int count=1;
    Switch switchCheck;
    TextInputLayout toplayout;
    TextView selectStatement;
    Spinner spinner;
    Button lab_report;
    ArrayList<Bitmap> lab_bitmap;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    String patient_id,appoiment_id;
    Loading_Dai loading_dai;
    TextInputEditText Disname,cheif_compalin,extra_oral,sign_symption,diagnose,intra_oral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_disease_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lab_bitmap=new ArrayList<Bitmap>();
        cheif_compalin=findViewById(R.id.cheif_compalin);
        extra_oral=findViewById(R.id.extra_oral);
        sign_symption=findViewById(R.id.sign_symption);
        diagnose=findViewById(R.id.diagnose);
        loading_dai=new Loading_Dai(this);
        topLinearLayout=findViewById(R.id.topLinearLayout);
        lab_report=findViewById(R.id.lab_report);
        switchCheck=findViewById(R.id.switchCheck);
        toplayout=findViewById(R.id.toplayout);
        intra_oral=findViewById(R.id.intra_oral);
        Disname=findViewById(R.id.Disname);
        selectStatement=findViewById(R.id.selectStatement);
        spinner=findViewById(R.id.spinner);
        helper=new DBHelper(this);

        patient_id=getIntent().getStringExtra("id");
        helper.createAppoiment(patient_id,"complete");
        appoiment_id=String.valueOf(helper.getLastAddedRowId());

        Cursor cursor=helper.getDisease();

        if(cursor.getCount() > 0 && cursor.moveToFirst())
        {
           while (cursor.moveToNext()){
               CheckBox checkBox=new CheckBox(this);
               checkBox.setText(String.valueOf(cursor.getString(1)));
               topLinearLayout.addView(checkBox);
               checkBox.setId(count);
               count++;
           }

            CheckBox none=new CheckBox(this);
            none.setText("None");
            topLinearLayout.addView(none);
            none.setId(R.id.none);

           cursor.close();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            Toasty.error(this,"No data found in database",Toasty.LENGTH_LONG,true).show();
        }

        lab_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lab_report.setBackgroundResource(R.drawable.successbuttonback);
                lab_report.setTextColor(Color.GRAY);
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(PatientDiseaseDetails.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage();
                }
            }
        });


        switchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    toplayout.setVisibility(View.VISIBLE);
                    selectStatement.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }
                else {
                   toplayout.setVisibility(View.GONE);
                    selectStatement.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                }
            }
        });

    }

    public void Next_Step(View view) {
        loading_dai.Show();
        String disease="";
        String dis_name;
        String level_of_statification;
        int count=topLinearLayout.getChildCount();
        for (int i=0; i<count; i++){
            CheckBox checkBox=(CheckBox) topLinearLayout.getChildAt(i);
            if (checkBox.isChecked()){
                disease+=checkBox.getText().toString() +",";
            }
        }

        if(check(Disname)){
            level_of_statification=spinner.getSelectedItem().toString();
            dis_name=Disname.getText().toString();
        }
        else {
            level_of_statification="none";
            dis_name="none";
        }

        if(!disease.trim().equals("") && check(cheif_compalin) && check(intra_oral) && check(extra_oral) && check(sign_symption) && check(diagnose)){
          if(!lab_bitmap.isEmpty()){
              for (int i=0; i<lab_bitmap.size(); i++){
                 byte[] img= HelperFunctions.ImageToByte(lab_bitmap.get(i));
                 helper.insertLabReport(img,patient_id,appoiment_id);
              }
          }

          if(helper.insertMedicalReport(patient_id,appoiment_id,disease,dis_name,level_of_statification,cheif_compalin.getText().toString(),
                  extra_oral.getText().toString(),sign_symption.getText().toString(),diagnose.getText().toString(),intra_oral.getText().toString())){
              Intent intent=new Intent(getApplicationContext(),Treatment_Plan.class);
              intent.putExtra("patient_id",patient_id);
              intent.putExtra("appoiment_id",appoiment_id);
              startActivity(intent);
              finish();
          }
          else{
              Toasty.error(PatientDiseaseDetails.this,"Error occurred in inserting medical report").show();
          }

        }
        else{
            Toasty.error(PatientDiseaseDetails.this,"Fill the values correctly").show();
        }

        loading_dai.Hide();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void SelectImage()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUST_CODE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(PatientDiseaseDetails.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // **********************************  End of method ****************************************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUST_CODE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                try{
                    if(!lab_bitmap.isEmpty())
                    {
                        lab_bitmap.clear();
                    }

                    if(data.getClipData() == null)
                    {
                        Uri u=data.getData();
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(u);
                        Bitmap bitmap= BitmapFactory.decodeStream(stream);
                        lab_bitmap.add(bitmap);
                        lab_report.setBackgroundResource(R.drawable.selected);
                        lab_report.setTextColor(Color.WHITE);
                    }
                    else {

                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for(int i = 0; i < count; i++)
                        {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream stream=getApplicationContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(stream);
                            lab_bitmap.add(bitmap);
                            lab_report.setBackgroundResource(R.drawable.selected);
                            lab_report.setTextColor(Color.WHITE);
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Error occurred select images again", Toast.LENGTH_SHORT).show();
                }
            }
        }

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