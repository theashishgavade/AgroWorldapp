package com.project.agroworld.taskmanager.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.project.agroworld.db.FarmerDAO;
import com.project.agroworld.db.FarmerDatabase;
import com.project.agroworld.db.FarmerModel;

import java.util.List;

public class DatabaseRepository {
    private FarmerDAO dao;
    private LiveData<List<FarmerModel>> farmerRoutines;
    private LiveData<Integer> maxIdCount;

    // creating a constructor for our variables
    // and passing the variables to it.
    public DatabaseRepository(Application application) {
        FarmerDatabase database = FarmerDatabase.getInstance(application);
        dao = database.taskDao();
        farmerRoutines = dao.getFarmerRoutines();
        maxIdCount = dao.getMaxCount();
    }

    // creating a method to insert the data to our database.
    public void insert(FarmerModel model) {
        new InsertCourseAsyncTask(dao).execute(model);
    }
    public void update(FarmerModel model) {
        new UpdateCourseAsyncTask(dao).execute(model);
    }

    public void delete(FarmerModel model) {
        new DeleteCourseAsyncTask(dao).execute(model);
    }
    public LiveData<List<FarmerModel>> getFarmerRoutines() {
        return farmerRoutines;
    }

    public LiveData<Integer> getMaxIdCount() {return maxIdCount;}
    private static class InsertCourseAsyncTask extends AsyncTask<FarmerModel, Void, Void> {
        private FarmerDAO dao;

        private InsertCourseAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FarmerModel... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<FarmerModel, Void, Void> {
        private FarmerDAO dao;

        private UpdateCourseAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FarmerModel... models) {
            dao.update(models[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<FarmerModel, Void, Void> {
        private FarmerDAO dao;

        private DeleteCourseAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FarmerModel... models) {
            dao.delete(models[0]);
            return null;
        }
    }
}
