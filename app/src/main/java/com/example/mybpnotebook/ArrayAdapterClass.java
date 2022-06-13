package com.example.mybpnotebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArrayAdapterClass extends RecyclerView.Adapter<ArrayAdapterClass.Holder> {
    ArrayList<modelRecord> bpRecord;
    Context context;

    public ArrayAdapterClass(Context context,ArrayList<modelRecord> bpRecord) {
        this.context = context;
        this.bpRecord = bpRecord;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_order,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        modelRecord modelRecord= bpRecord.get(position);

        String User=modelRecord.getUser();
        String date=modelRecord.getDate();
        String time=modelRecord.getTime();
        String sys=modelRecord.getSys();
        String dia=modelRecord.getDia();
        String status=modelRecord.getStatus();


        holder.tv_orderFor.setText(User);
        holder.tv_orderTime.setText(time);
        holder.tv_dia.setText("Diastolic : "+dia);
        holder.tv_sys.setText("Systalic  : " +sys);
        holder.tv_orderDate.setText(date);
        holder.tv_Status.setText(status);

        if(status.equals("Very High")){
            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if(status.equals("Very Low")){
            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.pink));
        }
        else if(status.equals("High")){
            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.little_red));
        }
        else if(status.equals("Low")){
            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else{
            holder.tv_Status.setTextColor(context.getResources().getColor(R.color.green));
        }

        if(Integer.parseInt(sys)>138){
            holder.tv_sys.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if(Integer.parseInt(sys)<105){
            holder.tv_sys.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else {
            holder.tv_sys.setTextColor(context.getResources().getColor(R.color.green));
        }

        if(Integer.parseInt(dia)>90){
            holder.tv_dia.setTextColor(context.getResources().getColor(R.color.red));
        }
        else if(Integer.parseInt(dia)<75){
            holder.tv_dia.setTextColor(context.getResources().getColor(R.color.yellow));
        }
        else {
            holder.tv_dia.setTextColor(context.getResources().getColor(R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return bpRecord.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_orderDate,tv_orderFor,tv_orderTime,tv_sys,tv_dia,tv_Status;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_orderFor=itemView.findViewById(R.id.tv_orderFor);
            tv_orderTime=itemView.findViewById(R.id.tv_orderTime);
            tv_orderDate=itemView.findViewById(R.id.tv_orderDate);
            tv_sys=itemView.findViewById(R.id.tv_sys);
            tv_dia=itemView.findViewById(R.id.tv_dia);
            tv_Status=itemView.findViewById(R.id.tv_Status);
        }
    }
}
