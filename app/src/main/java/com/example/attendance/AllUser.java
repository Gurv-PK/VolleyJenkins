package com.example.attendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

//Class to showcase list of Employees
public class AllUser extends AppCompatActivity {

    //Variable Declarations
    RecyclerView recyclerView;
    ArrayList<AttendancePojo> arrayList;
    File file;
    ProgressBar progressBar;
    UserAdaptor adp;
    AttendancePojo attendancePojo;
    RetorInstance retorInstance;
    DBHelper helper;
    Calendar cl;
    String myTime,myDate,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        recyclerView = findViewById(R.id.allrecycler);
        progressBar = findViewById(R.id.listprogress);
        arrayList = new ArrayList<>();
        retorInstance = RestApiClient.getUser().create(RetorInstance.class);
        helper = new DBHelper(this,DBHelper.DATABASE_NAME,null,1);

        adp = new UserAdaptor(AllUser.this,arrayList);
        recyclerView.setAdapter(adp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setData();

    }


    //Fetches ID and Image URL for us to pass to Retrofit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(AllUser.this, "File Captured", Toast.LENGTH_SHORT).show();

            //Image conversion from captured to bitmap to file format to send to API
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File f = new File(file.getAbsolutePath());
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(f));
                photo.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Retrive id from IDModal class
            id = IDModal.getID();

            //API requesting and parameters part
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody rId = RequestBody.create(MediaType.parse("multipart/form-data"), id);


            //Parameters that are going to API
            System.out.println("ID is"+id);
            System.out.println("file name is"+file.getName());
            System.out.println("Request File is"+requestFile);
            System.out.println("Image is"+body);
            retorInstance.CheckStatus(rId, body).enqueue(new Callback<APIResponse>() {


                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

//                    System.out.println("Response is"+ response);
                    Date date = new Date();
                    date.setTime(response.raw().receivedResponseAtMillis());
                    System.out.println("Response Time:");
                    myDate = new SimpleDateFormat("MM/dd/yyyy").format(date);
                    myTime = new SimpleDateFormat("hh:mm:ss").format(date);

                    IDModal.setTime(myTime);
                    IDModal.setDate(myDate);
                    if (response.body().getResponse().equals("Match"))
                    {
                        Toast.makeText(AllUser.this, "Check in Time:"+myTime, Toast.LENGTH_SHORT).show();
                        boolean isInsert = helper.mark(id,myDate,"Present",myTime,"");
                        if (isInsert) {
                            setData();
                        }
                        Toast.makeText(AllUser.this, "Marked Present", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        helper.mark(id,myDate,"Absent","-","-");
                        startActivity(new Intent(AllUser.this,DisplayActivity.class));
                        Toast.makeText(AllUser.this, "Marked Absent", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Toast.makeText(AllUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error is"+t.getMessage());
                    Toast.makeText(AllUser.this, "There was some issue on server side please try again", Toast.LENGTH_SHORT).show();
                }
            });


        }

        if (requestCode == 211 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(AllUser.this, "File Captured", Toast.LENGTH_SHORT).show();

            //Image conversion from captured to bitmap to file format to send to API
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File f = new File(file.getAbsolutePath());
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(f));
                photo.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            id = IDModal.getID();

            //API requesting and parameters part
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody rId = RequestBody.create(MediaType.parse("multipart/form-data"), id);

            //Parameters that are going to API

            retorInstance.UpdateEmployeeInfo(rId, body).enqueue(new Callback<APIResponse>() {

                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

//                    System.out.println("Response is"+ response);
                    if (response.body().getResponse().equals("Image saved successfully"))
                    {
                        Toast.makeText(AllUser.this, "Updated", Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(AllUser.this, "Not Updated", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Toast.makeText(AllUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error is"+t.getMessage());
                    Toast.makeText(AllUser.this, "There was some issue on our side", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

//
//        }



    public void setData()
    {
        arrayList.clear();
       adp.notifyDataSetChanged();
        for(int i=0;i<helper.getUsers().size();i++)
        {
            attendancePojo = new AttendancePojo();
            attendancePojo.setId(helper.getUsers().get(i).getId());
            attendancePojo.setName(helper.getUsers().get(i).getName());
            attendancePojo.setStatus(helper.Fetch_recent_status(helper.getUsers().get(i).getId(),myDate));
            attendancePojo.setCheckInTime(helper.Fetch_Check_IN(helper.getUsers().get(i).getId(),myDate));
            arrayList.add(attendancePojo);
        }
        adp.notifyDataSetChanged();
    }
    //Method for returning captured image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
        );
        return image;
    }


}