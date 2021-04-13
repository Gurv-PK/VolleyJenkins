package com.example.attendance;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.recyclerview.widget.RecyclerView;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import static android.Manifest.permission.CAMERA;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


//Adaptor for showing Employee List
public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.UserViewHolder> {

    Context ctx;
    Context mContext;
    ArrayList<AttendancePojo> arrlist;
    DBHelper helper;


    public UserAdaptor(Context ctx, ArrayList<AttendancePojo> arrlist) {
        this.ctx = ctx;
        this.arrlist = arrlist;
    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        helper = new DBHelper(ctx,DBHelper.DATABASE_NAME,null,1);
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.user_list,parent,false);
        return new UserViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.name.setText(arrlist.get(position).getName());
        holder.id.setText(arrlist.get(position).getId());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter ttf = DateTimeFormatter.ofPattern("hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));


//isko uncomment krne se time ke according disable ho jaata

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if(hour < 14)
        {
            if(!(helper.Fetch_recent_status(arrlist.get(position).getId(),dtf.format(now)).equals("Present")))
            {
                holder.btnopen.setText("Check In");
                holder.btnopen.setEnabled(true);
            }
            else
            {
                holder.btnopen.setText("Check Out");
                holder.btnopen.setEnabled(false);
            }
        }
        else
        {
            if(helper.Fetch_Check_out(arrlist.get(position).getId(),dtf.format(now)).equals(""))
            {
                holder.btnopen.setText("Check Out");
                holder.btnopen.setEnabled(true);
            }
            else
            {
                holder.btnopen.setText("Check Out");
                holder.btnopen.setEnabled(false);
            }

        }




        //Take Photo opens camera
        holder.btnopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.btnopen.getText().equals("Check In"))
                {
                    IDModal.setID(arrlist.get(position).getId());
                    //Camera Check and Open Functionlity
                    if (checkSelfPermission(mContext,CAMERA) != PermissionChecker.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(((Activity) mContext),new String[]{CAMERA}, 101);
                        ((Activity) mContext).startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),111);
                    }
                    else
                    {
                        ((Activity) mContext).startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),111);
                        //The Activity result is in Alluser class
                    }
                }
                else
                {
                        Toast.makeText(ctx, "Time of checkout is:"+ttf.format(now), Toast.LENGTH_SHORT).show();

                    if(!(helper.Fetch_Check_IN(arrlist.get(position).getId(),dtf.format(now)).equals("")))
                    {
                        helper.CheckOut(arrlist.get(position).getId(),dtf.format(now),ttf.format(now));
                    }
                    else
                    {
                        helper.StraightCheckoutMark(arrlist.get(position).getId(),dtf.format(now),"Present",ttf.format(now));
                        holder.btnopen.setText("Check Out");
                        holder.btnopen.setEnabled(false);
                    }
                        holder.btnopen.setEnabled(false);
                }



            }
        });

        holder.btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDModal.setID(arrlist.get(position).getId());
                ((Activity) mContext).startActivity(new Intent(ctx,DisplayActivity.class));
            }
        });

        holder.btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IDModal.setID(arrlist.get(position).getId());
                if (checkSelfPermission(mContext,CAMERA) != PermissionChecker.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(((Activity) mContext),new String[]{CAMERA}, 201);
                }
                else
                {
                    ((Activity) mContext).startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),211);
                    //The Activity result is in Alluser class
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrlist.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder{


        TextView name,id;
        Button btnopen,btnshow,btnupdate;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ename);
            id = itemView.findViewById(R.id.eid);
            btnopen = itemView.findViewById(R.id.btn_open);
            btnshow = itemView.findViewById(R.id.btn_show);
            btnupdate = itemView.findViewById(R.id.btn_update);
        }


    }

}