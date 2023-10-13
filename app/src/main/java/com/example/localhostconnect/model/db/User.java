package com.example.localhostconnect.model.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String lastname;
    public String birth_date;
    public int age;
    public String newColumn;
}
