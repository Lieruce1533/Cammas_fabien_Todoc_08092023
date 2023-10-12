package com.cleanup.todoc.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends AndroidViewModel {


    private final TodocRepository mTodocRepository;
  
    // MutableLiveData to aggregate the sorted tasks
    //----------------------------------------------------------------------------------------------

    private final MutableLiveData<List<Task>> aggregatedTasks = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mTodocRepository = TodocRepository.getInstance(application);

    }

    //-----Start of Logic-----------------------------------------------------------------------------------------

    /**
     * the new logic:
     * @return
     */
    public void handleSortingPreference(String preference) {
        mTodocRepository.setPreference(preference);
        Log.d("TAG in view-model", "handleSortingPreference: is triggered ");
        updateFilteredTasks();

    }

    public void updateFilteredTasks() {
        /*
        List<Task> tasks = mTodocRepository.getTaskToDisplay().getValue();

        Log.d("TAG viewModel", "Repository returned:  Mutable is updated");
        aggregatedTasks.setValue(tasks);
        */
        mTodocRepository.getTaskToDisplay().observeForever(new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                Log.d("TAG viewModel", "Repository returned:  Mutable is updated");
                aggregatedTasks.postValue(tasks);
            }
        });
    }
    //-----End of Logic-----------------------------------------------------------------------------------------

    /**
     * the live data to be observe by the UI
     *
     */
    public LiveData<List<Task>> getAggregatedTasks() {
        return aggregatedTasks;
    }
  
    LiveData<List<Project>> getAllProjects() { return mTodocRepository.getAllProjects();}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}

}