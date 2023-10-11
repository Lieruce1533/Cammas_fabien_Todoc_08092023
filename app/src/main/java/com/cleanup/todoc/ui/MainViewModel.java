package com.cleanup.todoc.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final TodocRepository mTodocRepository;
    //----------------------------------------------------------------------------------------------
    /*

    private static  LiveData<List<Task>> mAllTasks;
    private static  LiveData<List<Task>> mAllTasksSortedByTimeStampDesc;
    private static  LiveData<List<Task>> mAllTasksSortedByTimeStampAsc;
    private static  LiveData<List<Task>> mAllTasksSortedByNameAsc;
    private static  LiveData<List<Task>> mAllTasksSortedByNameDesc;
    */
    //----------------------------------------------------------------------------------------------
    // MediatorLiveData to aggregate the sorted tasks
    //----------------------------------------------------------------------------------------------

    private final MutableLiveData<List<Task>> aggregatedTasks = new MutableLiveData<>();

    //private final LiveData<List<Project>> mAllProjects;
    private String preference;





    public MainViewModel(@NonNull Application application) {
        super(application);
        mTodocRepository = TodocRepository.getInstance(application);
        preference = "All_Tasks";
        updateFilteredTasks();

        //----------------------------------------------------------------------------------------------
        /*
        mAllTasks = mTodocRepository.getAllTasks();
        mAllTasksSortedByTimeStampDesc = mTodocRepository.getAllTasksSortedByCreationTimestampDesc();
        mAllTasksSortedByTimeStampAsc = mTodocRepository.getAllTasksSortedByCreationTimestampAsc();
        mAllTasksSortedByNameAsc = mTodocRepository.getTasksOrderedByProjectName();
        mAllTasksSortedByNameDesc = mTodocRepository.getTasksOrderedByProjectNameDesc();
        mAllProjects = mTodocRepository.getAllProjects();
        */
        //----------------------------------------------------------------------------------------------
        // Set the initial source for aggregatedTasks (default sorting option)
        //setSortingPreference(SortingPreference.ALL_TASKS);
        //----------------------------------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------------------------
    //old logic
    /*
    public static void setSortingPreference(SortingPreference sortingPreference) {
        switch (sortingPreference) {
            case CREATION_TIMESTAMP_DESC:
                aggregatedTasks.addSource(mAllTasksSortedByTimeStampDesc, tasks -> aggregatedTasks.setValue(tasks));
                break;
            case CREATION_TIMESTAMP_ASC:
                aggregatedTasks.addSource(mAllTasksSortedByTimeStampAsc, tasks -> aggregatedTasks.setValue(tasks));
                break;
            case NAME_ASC:
                aggregatedTasks.addSource(mAllTasksSortedByNameAsc, tasks -> aggregatedTasks.setValue(tasks));
                break;
            case NAME_DESC:
                aggregatedTasks.addSource(mAllTasksSortedByNameDesc, tasks -> aggregatedTasks.setValue(tasks));
                break;
            case ALL_TASKS:
                aggregatedTasks.addSource(mAllTasks, tasks -> aggregatedTasks.setValue(tasks));
                break;
        }
    }
    */
    //----------------------------------------------------------------------------------------------

    /**
     * the new logic:
     * @return
     */
    public void handleSortingPreference(String preference) {
        this.preference = preference;
        Log.d("TAG in viewmodel", "handleSortingPreference: is trigerred ");
        updateFilteredTasks();
    }

    public void updateFilteredTasks() {
        List<Task> tasks = mTodocRepository.getTaskToDisplay(preference).getValue();

        Log.d("TAG viewModel", "onChanged:  in updateFilteredTasks is called");
        aggregatedTasks.setValue(tasks);

    }


    public LiveData<List<Task>> getAggregatedTasks() {
        return aggregatedTasks;
    }

    LiveData<List<Project>> getAllProjects() { return mTodocRepository.getAllProjects();}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}



}
