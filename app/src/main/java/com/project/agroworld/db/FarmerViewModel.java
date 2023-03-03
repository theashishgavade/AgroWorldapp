package com.project.agroworld.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.agroworld.db.repository.DatabaseRepository;

import java.io.Closeable;
import java.util.List;

public class FarmerViewModel extends AndroidViewModel {
    private DatabaseRepository repository;
    private LiveData<List<FarmerModel>> farmerRoutines;

    public FarmerViewModel(@NonNull Application application) {
        super(application);
        repository = new DatabaseRepository(application);
        farmerRoutines = repository.getFarmerRoutines();
    }

    public void insert(FarmerModel model) {repository.insert(model);}
    public void delete(FarmerModel model) {repository.delete(model);}
    public LiveData<List<FarmerModel>> getAllCourses() {
        return farmerRoutines;
    }
}