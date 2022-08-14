package com.ss_technology.dims.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ss_technology.dims.Helper.HelperFunctions;

public class DBHelper extends SQLiteOpenHelper {

    private static final String dbName="doctor.db";
    private static int dbVersion=1;

    public DBHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL("create table disease(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT UNIQUE);");
     db.execSQL("create table patient(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,mobile TEXT,address text,age INTEGER,ref text)");
     db.execSQL("create table appoiment(id INTEGER PRIMARY KEY AUTOINCREMENT,patient_id INTEGER,date TEXT,status TEXT,msg_status integer default(0), FOREIGN KEY(patient_id) REFERENCES patient(id));");
     db.execSQL("create table appoiment_images_report(id INTEGER PRIMARY KEY AUTOINCREMENT,image BLOB,patient_id INTEGER,appoiment_id INTEGER, FOREIGN KEY(patient_id) REFERENCES patient(id),FOREIGN KEY(appoiment_id) REFERENCES appoiment(id));");
     db.execSQL("create table medical_report(id INTEGER PRIMARY KEY AUTOINCREMENT,patient_id INTEGER,appoiment_id INTEGER,disease TEXT,previous_received_treatment text,level_satif TEXT,cheif_complain TEXT,extra_oral text,sign_symption TEXT,diagnose TEXT,intra_oral TEXT,FOREIGN KEY(patient_id) REFERENCES patient(id),FOREIGN KEY(appoiment_id) REFERENCES appoiment(id));");
     db.execSQL("create table medician(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,mg INTEGER,time TEXT,duration TEXT,type text,description text,taking_time TEXT,appoiment_id INTEGER,patient_id INTEGER,FOREIGN KEY(patient_id) REFERENCES patient(id),FOREIGN KEY(appoiment_id) REFERENCES appoiment(id));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+dbName);
        onCreate(db);
    }

    public boolean checkDiseaseList(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from disease",null);
        if(cursor.getCount() > 0)
        {
            cursor.close();
            return true;
        }
        else {
            cursor.close();
            return false;
        }
    }

    public void setInitialDisease()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("insert into disease(name) values('Blood Pressur'),('Heart Disease'),('Sugar')," +
                "('HBs'),('HCv'),('allergy');");
    }

    public boolean setDisese(String value){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",value);
        return (db.insert("disease",null,contentValues) > 0) ? true : false;
    }

    public Cursor getDisease()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from disease",null);
        return cursor;
    }

    public boolean insertPatientDetails(String name,String mobile,String address,String age,String ref){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("mobile",mobile.trim());
        contentValues.put("address",address);
        contentValues.put("age",age);
        contentValues.put("ref",ref);
        return (db.insert("patient",null,contentValues) > 0) ? true : false;
    }

    public boolean createAppoiment(String patient_id,String status){
        // status is complete (use for current appoinment)
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("patient_id",patient_id);
        contentValues.put("date", HelperFunctions.currentDate());
        contentValues.put("status",status);
        return (db.insert("appoiment",null,contentValues) > 0) ? true : false;
    }

    public boolean createAppoiment(String patient_id,String status,String date){
        //status is pending (use for next appoimnet)
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("patient_id",patient_id);
        contentValues.put("date", date);
        contentValues.put("status",status);
        return (db.insert("appoiment",null,contentValues) > 0) ? true : false;
    }

    public int getLastAddedRowId() {
        String queryLastRowInserted = "select last_insert_rowid()";
        SQLiteDatabase db=this.getReadableDatabase();
        final Cursor cursor = db.rawQuery(queryLastRowInserted, null);
        int _idLastInsertedRow = 0;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    _idLastInsertedRow = cursor.getInt(0);
                }
            } finally {
                cursor.close();
            }
        }

        return _idLastInsertedRow;
    }

    public boolean insertLabReport(byte[] image,String patient_id,String appoiment_id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("image",image);
        contentValues.put("patient_id", patient_id);
        contentValues.put("appoiment_id",appoiment_id);
        return (db.insert("appoiment_images_report",null,contentValues) > 0) ? true : false;
    }

    public boolean insertMedicalReport(String pat_id,String app_id,String disease,String previous_rec,String lev_sat,String cheif_com,
                                       String extra_oral,String sign_symption,String diagnose,String intra_oral){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("patient_id",pat_id);
        contentValues.put("appoiment_id",app_id);
        contentValues.put("disease",disease);
        contentValues.put("previous_received_treatment",previous_rec);
        contentValues.put("level_satif",lev_sat);
        contentValues.put("cheif_complain",cheif_com);
        contentValues.put("extra_oral",extra_oral);
        contentValues.put("sign_symption",sign_symption);
        contentValues.put("diagnose",diagnose);
        contentValues.put("intra_oral",intra_oral);
        return (db.insert("medical_report",null,contentValues) > 0) ? true : false;
    }


    public boolean insertTreatment(String name,String mg,String time_in_day,String duration,String type,
                                   String description,String taking_time_meal,String app_id,String pat_id){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",name);
        contentValues.put("mg",mg);
        contentValues.put("time",time_in_day);
        contentValues.put("duration",duration);
        contentValues.put("type",type);
        contentValues.put("description",description);
        contentValues.put("taking_time",taking_time_meal);
        contentValues.put("appoiment_id",app_id);
        contentValues.put("patient_id",pat_id);
        return (db.insert("medician",null,contentValues) > 0) ? true : false;
    }


    public Cursor getPatientDetails(String patient_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from patient where id="+patient_id,null);
        return cursor;
    }

    public Cursor getMedicalReport(String patient_id,String app_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from medical_report where patient_id="+patient_id+" and appoiment_id="+app_id,null);
        return cursor;
    }

    public Cursor getMedician(String patient_id,String app_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from medician where patient_id="+patient_id+" and appoiment_id="+app_id,null);
        return cursor;
    }

    public Cursor getAppoimentDetails(String app_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment where id="+app_id,null);
        return cursor;
    }

    public boolean getAppoimentDetailsWithStatus(String patient_id)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment where patient_id="+patient_id+" and status='pending'",null);
        if(cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public Cursor searchPatient(String query){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from patient where id="+query+" or mobile='"+query+"'",null);
        return cursor;
    }

    public Cursor searchAppoiment(String pat_id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment where patient_id="+pat_id+" order by id desc",null);
        return cursor;
    }

    public Cursor getMedicalReport(String pat_id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from medical_report where patient_id="+pat_id+" order by id desc",null);
        return cursor;
    }

    public Cursor getLabReport(String pat_id,String app_id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment_images_report where patient_id="+pat_id+" and appoiment_id="+app_id+" order by id desc",null);
        return cursor;
    }

    public Cursor getLabReport(String id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment_images_report where id="+id,null);
        return cursor;
    }

    public boolean updateAppoimentStatus(String appoiment_id){
        SQLiteDatabase db=this.getWritableDatabase();
        String status="complete";
        ContentValues contentValues=new ContentValues();
        contentValues.put("status",status);
        return (db.update("appoiment",contentValues,"id = ?", new String[]{appoiment_id}) > 0) ? true : false;
    }

    public Cursor getSMSappoimentList(String date){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from appoiment where date='"+date+"' and status='pending' and msg_status=0",null);
        return cursor;
    }

    public boolean updateAppoimentMessageStatus(String appoiment_id,int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        // 0 message not sended yet and 1 for message sended
        contentValues.put("msg_status",status);
        return (db.update("appoiment",contentValues,"id = ?", new String[]{appoiment_id}) > 0) ? true : false;
    }

}
