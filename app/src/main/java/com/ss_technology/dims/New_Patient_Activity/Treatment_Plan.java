package com.ss_technology.dims.New_Patient_Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.Helper.Loading_Dai;
import com.ss_technology.dims.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Treatment_Plan extends AppCompatActivity {

    LinearLayout medician_layout;
    Button addMedician;
    int i=0;
    ArrayList<LinearLayout> medicianList;
    String patient_id,appoimnet_id;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment__plan);

        init();
        patient_id=getIntent().getStringExtra("patient_id");
        appoimnet_id=getIntent().getStringExtra("appoiment_id");

        helper=new DBHelper(Treatment_Plan.this);
        addMedician.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Add_Medician_View(i);
                i++;
            }
        });

    }

    private void init(){
        medician_layout=findViewById(R.id.medician_layout);
        addMedician=findViewById(R.id.addMedician);
        medicianList=new ArrayList<>();
    }

    public void Clear_Medician(View view) {
        medician_layout.removeAllViews();
        if (!medicianList.isEmpty())
            medicianList.clear();
    }

    public void Save_data(View view) {

        if(medician_layout.getChildCount() > 0){
         for(int j=0; j<medicianList.size(); j++){
             LinearLayout med=(LinearLayout) medicianList.get(j);
             String name,mg,time_in_a_days,duration,description,type;
             EditText n=(EditText) med.getChildAt(0);
             name=n.getText().toString();
             EditText ty=(EditText) med.getChildAt(1);
             type=ty.getText().toString();
             EditText m=(EditText) med.getChildAt(2);
             mg=m.getText().toString();
             EditText t=(EditText) med.getChildAt(3);
             time_in_a_days=t.getText().toString();
             EditText d=(EditText) med.getChildAt(4);
             duration=d.getText().toString();
             EditText des=(EditText) med.getChildAt(5);
             description=des.getText().toString(); // mouth wash or other details

             try {
                 RadioGroup radioGroup=(RadioGroup) med.getChildAt(6);
                 RadioButton choic=findViewById(radioGroup.getCheckedRadioButtonId());
                 String meal=choic.getText().toString();
                 if (checkValue(duration) && checkValue(time_in_a_days) && checkValue(mg) && checkValue(name) && checkValue(meal)){
                     Loading_Dai loading_dai=new Loading_Dai(Treatment_Plan.this);
                     loading_dai.Show();
                     if(helper.insertTreatment(name,mg,time_in_a_days,duration,type,description,meal,appoimnet_id,patient_id)){
                        Intent intent=new Intent(getApplicationContext(),Next_Appoinment.class);
                        intent.putExtra("patient_id",patient_id);
                        intent.putExtra("appoiment_id",appoimnet_id);
                        startActivity(intent);
                        finish();
                     }
                     else{
                         Toasty.error(Treatment_Plan.this,"Something went wrong try again").show();
                     }
                     loading_dai.Hide();
                 }
                 else{
                     Toasty.error(Treatment_Plan.this,"Fill the values correctly!").show();
                     break;
                 }
             }
             catch (Exception e){
                 Toasty.error(Treatment_Plan.this,"Fill the values correctly!").show();
                 break;
             }
         }
        }
        else{
            Toasty.warning(Treatment_Plan.this,"Please add medicans first!").show();
        }
    }

    private int Dp_to_PX(int dp){
        int paddingDp = dp;
        float density = getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);
        return paddingPixel;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Add_Medician_View(int id){
        CardView cardView=new CardView(Treatment_Plan.this);
        cardView.setRadius(15);
        cardView.setElevation(12);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(Dp_to_PX(10),Dp_to_PX(10),Dp_to_PX(10),Dp_to_PX(10));
        cardView.setLayoutParams(layoutParams);

        LinearLayout main_layout=new LinearLayout(Treatment_Plan.this);
        main_layout.setLayoutParams(layoutParams);
        main_layout.setOrientation(LinearLayout.VERTICAL);
        main_layout.setPadding(Dp_to_PX(10),Dp_to_PX(10),Dp_to_PX(10),Dp_to_PX(10));

        EditText name=new EditText(Treatment_Plan.this);
        LinearLayout.LayoutParams namelayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Dp_to_PX(50));
        name.setLayoutParams(namelayoutParams);
        name.setHint("Medicine name");
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(name);

        EditText type=new EditText(Treatment_Plan.this);
        type.setLayoutParams(namelayoutParams);
        type.setHint("Type (tab,syrp)");
        type.setInputType(InputType.TYPE_CLASS_TEXT);
        type.setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(type);

        EditText mg=new EditText(Treatment_Plan.this);
        mg.setLayoutParams(namelayoutParams);
        mg.setHint("MG");
        mg.setInputType(InputType.TYPE_CLASS_NUMBER);
        mg.setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(mg);

        EditText time_in_day=new EditText(Treatment_Plan.this);
        time_in_day.setLayoutParams(namelayoutParams);
        time_in_day.setHint("Times in a day");
        time_in_day.setInputType(InputType.TYPE_CLASS_NUMBER);
        time_in_day.setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(time_in_day);

        EditText duration =new EditText(Treatment_Plan.this);
        duration .setLayoutParams(namelayoutParams);
        duration .setHint("Duration ");
        duration.setInputType(InputType.TYPE_CLASS_TEXT);
        duration .setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(duration);

        EditText des =new EditText(Treatment_Plan.this);
        des .setLayoutParams(namelayoutParams);
        des .setHint("Description ");
        des.setInputType(InputType.TYPE_CLASS_TEXT);
        des .setPadding(Dp_to_PX(10),0,0,0);
        main_layout.addView(des);

        RadioGroup group=new RadioGroup(Treatment_Plan.this);
        LinearLayout.LayoutParams radioLayoutParms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radioLayoutParms.setMargins(0,Dp_to_PX(10),0,0);
        group.setLayoutParams(radioLayoutParms);

        RadioButton radio1=new RadioButton(Treatment_Plan.this);
        radio1.setText("کھانے کے بعد");
        LinearLayout.LayoutParams radiobtn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        radio1.setLayoutParams(radiobtn);
        group.addView(radio1);

        RadioButton radio2=new RadioButton(Treatment_Plan.this);
        radio2.setText("کھانے سے پہلے");
        radio2.setLayoutParams(radiobtn);
        group.addView(radio2);

        main_layout.addView(group);
        medicianList.add(main_layout);
        cardView.addView(main_layout);
        medician_layout.addView(cardView);
    }

    private boolean checkValue(String val){
        return (val.trim().length() > 0) ? true : false;
    }

    public void Skip(View view) {
        Intent intent=new Intent(getApplicationContext(),Next_Appoinment.class);
        intent.putExtra("patient_id",patient_id);
        intent.putExtra("appoiment_id",appoimnet_id);
        startActivity(intent);
        finish();
    }
}