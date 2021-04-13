package com.example.attendance;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity {

    //Declarations
    EditText name;
    Button binsert, bshow;
    DBHelper helper;
    int eid;
    File file;
    RetorInstance retorInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.name);
        binsert = findViewById(R.id.btn_insert);
        bshow = findViewById(R.id.btn_allemployees);
        retorInstance = RestApiClient.getUser().create(RetorInstance.class);
        Random random = new Random();
        helper = new DBHelper(this, DBHelper.DATABASE_NAME, null, 1);
        IDModal.setStatus("Absent");

        //Add User(Necessary before you mark their attendance)
        binsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eid = random.nextInt(30000);
                if(name.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "Enter Name First", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new Builder(MainActivity.this)
                            .setTitle("Open Camera")
                            .setMessage("You need to take a picture Press Ok to continue")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    if (ActivityCompat.checkSelfPermission(MainActivity.this,CAMERA) != PermissionChecker.PERMISSION_GRANTED)
                                    {
                                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{CAMERA}, 101);
                                    }
                                    else
                                    {
                                        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),111);
                                    }

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binsert.setEnabled(true);
            }
        });

        //Show Employee list
        bshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, AllUser.class));
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(MainActivity.this, "File Captured", Toast.LENGTH_SHORT).show();

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


            //API requesting and parameters part
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            RequestBody rId = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(eid));

            //Parameters that are going to API
            System.out.println("ID is"+eid);
            System.out.println("file name is"+file.getName());
            System.out.println("Request File is"+requestFile);
            System.out.println("Image is"+body);
            retorInstance.InsertEmployeeInfo(rId, body).enqueue(new Callback<APIResponse>() {


                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {

                    //if(!response.isSuccessful()){return;}
                    System.out.println("Response is"+ response);
                    if (response.body().getResponse().equals("Image saved successfully"))
                    {
                        boolean result = helper.insert(String.valueOf(eid), name.getText().toString());
                        if(result)
                        {
                            IDModal.setName(name.getText().toString());
                            IDModal.setID(String.valueOf(eid));
                            IDModal.setStatus("Absent");
                            binsert.setEnabled(false);
                            name.setText("");
                            Toast.makeText(MainActivity.this, "Succesfull", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            System.out.println("Result is:"+result);
                            Toast.makeText(MainActivity.this, "Not a Valid ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error is"+t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Response", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

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