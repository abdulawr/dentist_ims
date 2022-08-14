package com.ss_technology.dims.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.dims.R;

import java.util.List;

public class Medicine_Adapter extends RecyclerView.Adapter<Medicine_Adapter.View_Holder> {
    List<Medician_Container> list;
    Context context;

    public Medicine_Adapter(List<Medician_Container> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new View_Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_adapter_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View_Holder holder, int position) {
     Medician_Container data=list.get(position);
     holder.name.setText(data.getType()+" :  "+data.getName()+"   "+data.getMg()+"mg");
     holder.time_in_day.setText(data.getTime_in_day()+" time in a day");
     holder.taking_time.setText(data.getTaking_time());
     holder.duration.setText("For "+data.getDuration());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class View_Holder extends RecyclerView.ViewHolder {
        TextView name,time_in_day,taking_time,duration;
        public View_Holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            time_in_day=itemView.findViewById(R.id.time_in_day);
            taking_time=itemView.findViewById(R.id.taking_time);
            duration=itemView.findViewById(R.id.duration);
        }
    }
}
