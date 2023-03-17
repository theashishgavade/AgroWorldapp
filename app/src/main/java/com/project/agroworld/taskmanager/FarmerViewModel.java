package com.project.agroworld.taskmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.agroworld.db.FarmerModel;
import com.project.agroworld.taskmanager.repository.DatabaseRepository;

import java.util.List;

public class FarmerViewModel extends AndroidViewModel {
    private DatabaseRepository repository;
    private LiveData<List<FarmerModel>> farmerRoutines;
    private LiveData<Integer> maxIDCount;

    public FarmerViewModel(@NonNull Application application) {
        super(application);
        repository = new DatabaseRepository(application);
        farmerRoutines = repository.getFarmerRoutines();
        maxIDCount = repository.getMaxIdCount();
    }

    public void insert(FarmerModel model) {
        repository.insert(model);
    }

    public void delete(FarmerModel model) {
        repository.delete(model);
    }

    public void deleteAllData(){
        repository.deleteAllCourses();
    }

    public LiveData<List<FarmerModel>> getRoutineList() {
        return farmerRoutines;
    }

    public LiveData<Integer> getMaxIDCount() {
        return maxIDCount;
    }
}