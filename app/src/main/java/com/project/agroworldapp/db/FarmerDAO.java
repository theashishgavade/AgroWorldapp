package com.project.agroworldapp.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FarmerDAO {

    @Insert
    void insert(FarmerModel model);

    @Update
    void update(FarmerModel model);

    @Delete
    void delete(FarmerModel model);

    @Query("DELETE FROM farmer_table")
    void deleteRoutines();

    @Query("SELECT * FROM farmer_table ORDER BY id DESC")
    LiveData<List<FarmerModel>> getFarmerRoutines();

    @Query("SELECT MAX(id) FROM farmer_table")
    LiveData<Integer> getMaxCount();


}