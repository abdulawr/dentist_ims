package com.ss_technology.dims.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.dims.Database.DBHelper;
import com.ss_technology.dims.R;

import es.dmoral.toasty.Toasty;

public class AddNewDisease extends BottomSheetDialogFragment {

    Context context;

    public AddNewDisease(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.add_disease,container);
        DBHelper helper=new DBHelper(context);
        ImageButton closeIcon=view.findViewById(R.id.closeIcon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
            }
        });

        TextInputEditText name=view.findViewById(R.id.name);
        Button save=view.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameVal=name.getText().toString();
                if(!TextUtils.isEmpty(nameVal))
                {
                 if(helper.setDisese(nameVal))
                 {
                     dismiss();
                     Toasty.success(context,"Disease added successfully",Toast.LENGTH_LONG,true).show();
                 }
                 else {
                     Toasty.warning(context,"Value not inserted (Duplicated values is not allowed)", Toast.LENGTH_LONG,true).show();
                 }
                }
                else {
                    Toasty.error(context,"Please enter value first", Toast.LENGTH_LONG,true).show();
                }
            }
        });

        return view;
    }
}
