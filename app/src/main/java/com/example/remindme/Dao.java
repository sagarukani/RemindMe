package com.example.remindme;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@androidx.room.Dao
public interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes model);

    @Update
    void update(Notes model);

    @Delete
    void delete(Notes model);

    @Query("SELECT * FROM notes ORDER BY date ASC")
    LiveData<List<Notes>> getAllCourses();

    @Query("SELECT * FROM notes WHERE title LIKE :search")
    LiveData<List<Notes>> search(String search);
}
