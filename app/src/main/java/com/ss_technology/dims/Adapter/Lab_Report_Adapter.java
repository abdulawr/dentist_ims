package com.ss_technology.dims.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ss_technology.dims.R;

import java.util.List;

public class Lab_Report_Adapter extends RecyclerView.Adapter<Lab_Report_Adapter.ViewHolder> {

    List<Lab_Report_Container> list;
    Context context;
    OnClick_Card onClick_card;

    public Lab_Report_Adapter(List<Lab_Report_Container> list, Context context,OnClick_Card onClick_card) {
        this.list = list;
        this.context = context;
        this.onClick_card=onClick_card;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_report_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     holder.image.setImageBitmap(list.get(position).getBitmap());
     holder.card.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             onClick_card.getid(list.get(position).getId());
         }
     });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            card=itemView.findViewById(R.id.card);
        }
    }

   public interface OnClick_Card{
        void getid(String id);
    }
}
