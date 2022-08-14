package com.ss_technology.dims.New_Patient_Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ss_technology.dims.Activities.MainActivity;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Helper.HelperFunctions;
import com.ss_technology.dims.Helper.PdfDocumentAdapter;
import com.ss_technology.dims.R;

import java.io.File;
import java.io.FileOutputStream;

import es.dmoral.toasty.Toasty;

public class PrintDetails extends AppCompatActivity {

    final  int REQUEST_CODE=121;
    Bitmap header,header_result;
    Bitmap footer,footer_result;
    String patient_id,appoimnet_id;
    DBHelper helper;
    Cursor patient_details,appoiment_cursor,medican_cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_details);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PrintDetails.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }

        patient_id=getIntent().getStringExtra("patient_id");
        appoimnet_id=getIntent().getStringExtra("appoiment_id");
        helper=new DBHelper(this);

        header=BitmapFactory.decodeResource(getResources(),R.drawable.header);
        header_result=Bitmap.createScaledBitmap(header,864,190,false);

        footer=BitmapFactory.decodeResource(getResources(),R.drawable.footer);
        footer_result=Bitmap.createScaledBitmap(footer,864,110,false);

        // to get patient details from DB
        patient_details=helper.getPatientDetails(patient_id);
        patient_details.moveToFirst();
        appoiment_cursor=helper.getAppoimentDetails(appoimnet_id);
        appoiment_cursor.moveToFirst();
        medican_cursor=helper.getMedician(patient_id,appoimnet_id);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Generate_PDF(View view) {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PrintDetails.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else {
           Print_Paper();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void Print_Paper(){

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(864, 1222, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Paint paint=new Paint();
        Canvas canvas=page.getCanvas();
        canvas.drawBitmap(header_result,0,10,paint);
        canvas.drawBitmap(footer_result,0,page.getInfo().getPageHeight()-110,paint);

        paint.setColor(getResources().getColor(R.color.paper_text));
        paint.setTextSize(16f);
        paint.setFakeBoldText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawLine(200,250,200,page.getInfo().getPageHeight()-110,paint);
        canvas.drawText("Patient`s Name",40,230,paint);
        paint.setFakeBoldText(false);
        //patient name
        canvas.drawText(patient_details.getString(patient_details.getColumnIndex("name")),165,231,paint);
        canvas.drawLine(160,234,395,234,paint);
        paint.setFakeBoldText(true);
        canvas.drawText("Date",420,230,paint);
        paint.setFakeBoldText(false);
        // check up date
        canvas.drawText(appoiment_cursor.getString(appoiment_cursor.getColumnIndex("date")),470,231,paint);
        canvas.drawLine(460,234,590,234,paint);

        canvas.drawText("Patient`s ID",620,230,paint);
        canvas.drawText(String.valueOf(patient_details.getInt(patient_details.getColumnIndex("id"))),720,231,paint);
        canvas.drawLine(710,234,800,234,paint);

        int size = medican_cursor.getCount();
        int startX=240,startY=290;
        try {
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    medican_cursor.moveToNext();

                    String name_details = medican_cursor.getString(medican_cursor.getColumnIndex("type")) + ":  " +
                            medican_cursor.getString(medican_cursor.getColumnIndex("name")) + "   " +
                            medican_cursor.getInt(medican_cursor.getColumnIndex("mg")) + " mg" +
                            "    " + medican_cursor.getString(medican_cursor.getColumnIndex("taking_time")) +
                            "     " + medican_cursor.getString(medican_cursor.getColumnIndex("description"));

                    String tk_time="";
                    int count=medican_cursor.getInt(medican_cursor.getColumnIndex("time"));
                    for(int j=0; j<count; j++){
                      if(j == count-1){
                        tk_time+=" 1";
                      }
                      else{
                          tk_time+=" 1 +";
                      }
                    }

                    String des=tk_time+"     "+"(For "+medican_cursor.getString(medican_cursor.getColumnIndex("duration"))+")";

                    paint.setColor(getResources().getColor(R.color.black));
                    paint.setFakeBoldText(false);
                    paint.setTextSize(14f);
                    canvas.drawText(name_details, startX, startY, paint);
                    paint.setTextSize(12f);
                    paint.setColor(getResources().getColor(R.color.paper_text));
                    canvas.drawText(des, 280, startY + 20, paint);

                    startY += 50;
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Problem: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        document.finishPage(page);

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/patients/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        String file_name=patient_details.getString(patient_details.getColumnIndex("name"))
                +"__"+patient_details.getString(patient_details.getColumnIndex("mobile"))+".pdf";
        String targetPdf = directory_path + file_name;
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Document created successfully and save to patients folder", Toast.LENGTH_LONG).show();

            PrintManager printManager=(PrintManager) PrintDetails.this.getSystemService(Context.PRINT_SERVICE);
            try
            {
                PrintDocumentAdapter printAdapter = new PdfDocumentAdapter(PrintDetails.this,directory_path+file_name,file_name);
              printManager.print("Document", printAdapter,new PrintAttributes.Builder().build());
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Problem: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //Print_Paper();
            }
            else {
                ActivityCompat.requestPermissions(PrintDetails.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void Done(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}