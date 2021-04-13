package com.example.attendance;



import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RetrofitTest {

    @InjectMocks
    private AllUser allUser;
    private APIResponse apiResponse;


    @Mock
    private RetorInstance retorInstance;
//    private Response<APIResponse> response;
//    private Call<APIResponse> responseCall;


    private static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    @Test
    public void testCheckStatus() {
        CountDownLatch latch = new CountDownLatch(1);

        System .out.println("Test Initiated");
        String id = "User101";
        File file = new File("src\\test\\resources\\Kangana.jpg");
        File successfile = getFileFromPath(this,"success.json");
        assertThat(successfile, notNullValue());
        retorInstance = RestApiClient.getUser().create(RetorInstance.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody rId = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        System.out.println("RetorInstance is:"+retorInstance);
        System .out.println("Parameter rid:"+rId);
        System .out.println("Parameter id:"+id);
        retorInstance.CheckStatus(rId,body).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                System.out.println("Present");
                latch.countDown();
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertEmployee() {
        CountDownLatch latch = new CountDownLatch(1);

        System .out.println("Insert Test Initiated");
        String id = "User101";
        File file = new File("src\\test\\resources\\Kangana.jpg");
        File successfile = getFileFromPath(this,"success.json");
        assertThat(successfile, notNullValue());
        retorInstance = RestApiClient.getUser().create(RetorInstance.class);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody rId = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        System.out.println("RetorInstance is:"+retorInstance);
        System .out.println("Parameter rid:"+rId);
        System .out.println("Parameter id:"+id);
        retorInstance.InsertEmployeeInfo(rId,body).enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                System.out.println("Present");
                latch.countDown();
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

