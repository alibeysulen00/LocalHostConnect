package com.example.localhostconnect.api;

import com.example.localhostconnect.model.UserData;

public interface OnDataReceivedListener {
    void onDataReceived(UserData userData);
    void onDataError();
}
