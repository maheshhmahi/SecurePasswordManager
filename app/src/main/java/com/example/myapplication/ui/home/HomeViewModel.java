package com.example.myapplication.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.ui.AppDatabase;
import com.example.myapplication.ui.User;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private final LiveData<List<User>> allUsers;

    public HomeViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getDatabase(application);
        allUsers = database.userDao().getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
