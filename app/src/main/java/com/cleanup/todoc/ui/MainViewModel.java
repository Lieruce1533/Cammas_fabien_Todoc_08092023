package com.cleanup.todoc.ui;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends ViewModel {


    private final TodocRepository mTodocRepository;
  
    // MutableLiveData to display view of tasks in UI.
    private final MutableLiveData<Boolean> isNull = new MutableLiveData<>();
    private LiveData<List<Task>> aggregatedTasks;



    public MainViewModel(@NonNull TodocRepository mtodocRepository) {

        this.mTodocRepository = mtodocRepository;

    }

     /**
     * the new logic:
     * @return
     */
    public void handleSortingPreference(String preference) {

        Log.d("TAG in view-model", "handleSortingPreference: is triggered ");

        mTodocRepository.onSortingTypeChanged(preference);

        Log.d("TAG in view-model", "handleSortingPreference: mutable updated");


    }
    public void updateIsNull() {
        List<Task> returnedTasks = aggregatedTasks.getValue();
        if (returnedTasks != null && returnedTasks.size() == 0) {
            isNull.setValue(true);
        }else{
            isNull.setValue(false);
        }
    }
    /**
     * the live data to be observe by the UI
     *
     */
    public LiveData<List<Task>> getAggregatedTasks() {
        aggregatedTasks = mTodocRepository.getTasksLiveData();
        Log.d("TAG in view-model", "getSortedTasks: value is");
        return aggregatedTasks;

    }
    public LiveData<Boolean> getIsNull() {
        Log.d("TAG in view-model", "getIsNull: is triggered ");
        return isNull;
    }
  
    LiveData<List<Project>> getAllProjects() { return mTodocRepository.getAllProjects();}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}

}