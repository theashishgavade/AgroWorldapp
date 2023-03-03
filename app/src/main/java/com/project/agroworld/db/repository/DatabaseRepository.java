package com.project.agroworld.db.repository;

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

    // creating a constructor for our variables
    // and passing the variables to it.
    public DatabaseRepository(Application application) {
        FarmerDatabase database = FarmerDatabase.getInstance(application);
        dao = database.taskDao();
        farmerRoutines = dao.getFarmerRoutines();
    }

    // creating a method to insert the data to our database.
    public void insert(FarmerModel model) {
        new InsertCourseAsyncTask(dao).execute(model);
    }

    // creating a method to update data in database.
    public void update(FarmerModel model) {
        new UpdateCourseAsyncTask(dao).execute(model);
    }

    // creating a method to delete the data in our database.
    public void delete(FarmerModel model) {
        new DeleteCourseAsyncTask(dao).execute(model);
    }

    // below is the method to delete all the courses.
    public void deleteAllCourses() {
        new DeleteAllCoursesAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public LiveData<List<FarmerModel>> getFarmerRoutines() {
        return farmerRoutines;
    }

    // we are creating a async task method to insert new course.
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

    // we are creating a async task method to update our course.
    private static class UpdateCourseAsyncTask extends AsyncTask<FarmerModel, Void, Void> {
        private FarmerDAO dao;

        private UpdateCourseAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FarmerModel... models) {
            // below line is use to update
            // our modal in dao.
            dao.update(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete course.
    private static class DeleteCourseAsyncTask extends AsyncTask<FarmerModel, Void, Void> {
        private FarmerDAO dao;

        private DeleteCourseAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(FarmerModel... models) {
            // below line is use to delete
            // our course modal in dao.
            dao.delete(models[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all courses.
    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private FarmerDAO dao;

        private DeleteAllCoursesAsyncTask(FarmerDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteRoutines();
            return null;
        }
    }
}
