package com.example.localhostconnect.controller;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.example.localhostconnect.api.ApiService;
import com.example.localhostconnect.api.OnDataReceivedListener;
import com.example.localhostconnect.api.RetrofitClient;
import com.example.localhostconnect.model.UserData;
import com.example.localhostconnect.model.db.AppDatabase;
import com.example.localhostconnect.model.db.User;
import com.example.localhostconnect.model.db.UserDao;
import com.example.localhostconnect.view.UserAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserController {
    private Context context;
    private List<UserData> userDataList;
    private UserAdapter userAdapter;
    private AppDatabase db;

    public UserController(Context context, List<UserData> userDataList, UserAdapter userAdapter, AppDatabase db) {
        this.context = context;
        this.userDataList = userDataList;
        this.userAdapter = userAdapter;
        this.db = db;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getUserDataByName(final OnDataReceivedListener listener, String name){
        if (isInternetAvailable()) {
            String baseUrl = "http://192.168.1.111:8080/";
            ApiService apiService = RetrofitClient.getClient(baseUrl).create(ApiService.class);

            Call<List<UserData>> call = apiService.getUserDataByName(name);
            call.enqueue(new Callback<List<UserData>>() {
                @Override
                public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {

                    if (response.isSuccessful()){
                        List<UserData> userDataList = response.body();
                        for (UserData userData : userDataList){
                            Log.d("TAG", "onResponse: " + userData.getName());
                            listener.onDataReceived(userData);
                        }
                    }
                    else{
                        listener.onDataError();
                    }
                }

                @Override
                public void onFailure(Call<List<UserData>> call, Throwable t) {
                    t.printStackTrace();
                    listener.onDataError();
                }
            });
        } else {
            // Internet bağlantısı yok, yerel veritabanından veriyi al
            new GetUsersAsyncTask(db.userDao(), listener).execute();
        }
    }

    private class GetUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {
        private UserDao userDao;
        private OnDataReceivedListener listener;

        private GetUsersAsyncTask(UserDao userDao, OnDataReceivedListener listener) {
            this.userDao = userDao;
            this.listener = listener;
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            return userDao.getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> userList) {
            super.onPostExecute(userList);
            userDataList.clear();
            for (User u : userList) {
                UserData userData1 = new UserData();
                userData1.setId(u.id);
                userData1.setName(u.name);
                userData1.setLastname(u.lastname);
                userData1.setBirth_date(u.birth_date);
                userData1.setAge(u.age);
                userDataList.add(userData1);
            }
            userAdapter.notifyDataSetChanged();
            if (userList.isEmpty()) {
                listener.onDataError();
            }
        }
    }
}
