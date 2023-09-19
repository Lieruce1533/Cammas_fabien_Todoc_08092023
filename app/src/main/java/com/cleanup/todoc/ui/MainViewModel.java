package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private TodocRepository mTodocRepository;
    private final LiveData<List<Task>> mAllTasks;
    private final LiveData<List<Project>> mAllProjects;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mTodocRepository = TodocRepository.getInstance(application);
        mAllTasks = mTodocRepository.getAllTasks();
        mAllProjects = mTodocRepository.getAllProjects();
    }

    LiveData<List<Task>> getAllTasks(){ return mAllTasks;}

    LiveData<List<Project>> getAllProjects() { return mAllProjects;}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}

}
