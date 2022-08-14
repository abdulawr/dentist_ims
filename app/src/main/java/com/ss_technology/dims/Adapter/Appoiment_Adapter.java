package com.ss_technology.dims.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.dims.Next_Appoimient_Activity.View_Appoiment_Details;
import com.ss_technology.dims.R;

import java.util.List;

public class Appoiment_Adapter extends RecyclerView.Adapter<Appoiment_Adapter.viewHolder> {

    List<Appoiment_Container> list;
    Context context;

    public Appoiment_Adapter(List<Appoiment_Container> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.appoiment_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
     Appoiment_Container data=list.get(position);
     holder.date.setText(data.getDate());
     holder.status.setText(data.getStatus());
     if(data.getStatus().trim().equals("pending")){
         holder.status.setTextColor(Color.parseColor("#4CAF50"));
     }
     else {
         holder.status.setTextColor(Color.parseColor("#FFC107"));
     }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date,status;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date);
            status=itemView.findViewById(R.id.status);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            Appoiment_Container d=list.get(getLayoutPosition());
            Intent intent=new Intent(context, View_Appoiment_Details.class);
            intent.putExtra("obj",d);
            context.startActivity(intent);
        }
    }
}
