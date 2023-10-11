package com.cleanup.todoc.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final TodocRepository mTodocRepository;
    private final MutableLiveData<List<Task>> filteredTasks = new MutableLiveData<>();

    private String preference;



    public MainViewModel(TodocRepository mTodocRepository) {
           this.mTodocRepository = mTodocRepository;
    }

    public void handleSortingPreference(String preference) {
        this.preference = preference;
        Log.d("TAG in viewmodel", "handleSortingPreference: is trigerred ");
         updateFilteredTasks();
    }

    public void updateFilteredTasks() {
        List<Task> tasks = mTodocRepository.getTaskToDisplay(preference).getValue();

                Log.d("TAG viewModel", "onChanged:  in updateFilteredTasks is called");
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
