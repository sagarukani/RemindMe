package com.example.remindme;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HomeViewModal extends AndroidViewModel {

    // creating a new variable for course repository.
    private ReminderRepository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<Notes>> allCourses;
    private LiveData<List<Notes>> searchList;

    // constructor for our view modal.
    public HomeViewModal(@NonNull Application application) {
        super(application);
        repository = new ReminderRepository(application);
        allCourses = repository.getAllReminders();
    }

    public LiveData<List<Notes>> search(String search){
        searchList = repository.getSearchList(search);
        return searchList;
    }

    // below method is use to insert the data to our repository.
    public void insert(Notes model) {
        repository.insert(model);
    }

    // below line is to delete the data in our repository.
    public void delete(Notes model) {
        repository.delete(model);
    }
    // below method is to get all the courses in our list.
    public LiveData<List<Notes>> getAllCourses() {
        return allCourses;
    }
}

