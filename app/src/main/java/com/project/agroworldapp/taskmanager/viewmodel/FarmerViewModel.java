package com.project.agroworldapp.taskmanager.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.agroworldapp.db.FarmerModel;
import com.project.agroworldapp.taskmanager.repository.DatabaseRepository;

import java.util.List;

public class FarmerViewModel extends AndroidViewModel {
    private final DatabaseRepository repository;
    private final LiveData<List<FarmerModel>> farmerRoutines;

    public FarmerViewModel(@NonNull Application application) {
        super(application);
        repository = new DatabaseRepository(application);
        farmerRoutines = repository.getFarmerRoutines();
        LiveData<Integer> maxIDCount = repository.getMaxIdCount();
    }

    public void insert(FarmerModel model) {
        repository.insert(model);
    }

    public void delete(FarmerModel model) {
        repository.delete(model);
    }

    public void deleteAllData() {
        repository.deleteAllCourses();
    }

    public LiveData<List<FarmerModel>> getRoutineList() {
        return farmerRoutines;
    }

    public LiveData<Integer> getMaxIDCount() {
        return repository.getMaxIdCount();
    }
}