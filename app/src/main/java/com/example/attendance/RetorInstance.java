package com.example.attendance;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetorInstance {

    @Multipart
    @POST("/face")
    Call<APIResponse> CheckStatus(@Part("userID") RequestBody EID, @Part MultipartBody.Part image);

    @Multipart
    @POST("/upload")
    Call<APIResponse> InsertEmployeeInfo(@Part("userID") RequestBody EID, @Part MultipartBody.Part image);

    @Multipart
    @POST("/upload")
    Call<APIResponse> UpdateEmployeeInfo(@Part("userID") RequestBody EID, @Part MultipartBody.Part image);


}
