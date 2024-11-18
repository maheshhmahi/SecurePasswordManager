package com.example.myapplication.ui;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM user ORDER BY website ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM user")
    List<User> getUsersDirect();
}