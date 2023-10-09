package com.cleanup.todoc.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final TodocRepository mTodocRepository;
    private static MutableLiveData<List<Task>> filteredTasks = new MutableLiveData<>(new ArrayList<>());



    public MainViewModel(@NonNull Application application) {
        super(application);
        mTodocRepository = TodocRepository.getInstance(application);

    }

    public static void updateFilteredTasks(String preference) {
        Log.d("TAG ViewModel asks Repo", "updateFilteredTasks: triggered");
        List<Task> tasks = TodocRepository.getTaskToDisplay(preference).getValue();
        Log.d("TAG ViewModel", "Received tasks: " + tasks.size());
        filteredTasks.setValue(tasks);


    }
    public LiveData<List<Task>> getFilteredTasks() {
        Log.d("TAG ViewModel", "getFilteredTasks: is called");
        return filteredTasks;
    }

    public LiveData<List<Project>> getAllProjects() {
        return mTodocRepository.getAllProjects();
    }

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}



}
