package com.example.localhostconnect.api;

import com.example.localhostconnect.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("user/getAll") //ENDPOINT

    Call<List<UserData>> getUserDataByName(@Query("name") String name);
}
