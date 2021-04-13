package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

public class AttendanceAdaptor extends RecyclerView.Adapter<AttendanceAdaptor.AttendanceViewHolder> {
    Context ctx;
    ArrayList<AttendancePojo> arrayList;
    DBHelper helper;

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        helper = new DBHelper(ctx,DBHelper.DATABASE_NAME,null,1);
        View view = inflater.inflate(R.layout.item_list,parent,false);
        return new AttendanceViewHolder(view);
    }

    public AttendanceAdaptor(Context ctx, ArrayList<AttendancePojo> arrayList) {
        this.ctx = ctx;
        this.arrayList = arrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, int position) {
        //set text into
        holder.date.setText(arrayList.get(position).getDate());
        holder.status.setText(arrayList.get(position).getStatus());
        holder.id.setText(arrayList.get(position).getId());
        holder.checkin.setText(arrayList.get(position).getCheckInTime());
        holder.checkout.setText(arrayList.get(position).getCheckOutTime());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder{

        TextView date,status,id,checkin,checkout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.ldate);
            status = itemView.findViewById(R.id.lstatus);
            id = itemView.findViewById(R.id.eid);
            checkin = itemView.findViewById(R.id.checkintime);
            checkout = itemView.findViewById(R.id.checkouttime);


        }
    }
}
