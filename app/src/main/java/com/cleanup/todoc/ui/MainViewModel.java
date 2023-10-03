package com.cleanup.todoc.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.cleanup.todoc.Repository.TodocRepository;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private TodocRepository mTodocRepository;
    private final LiveData<List<Task>> mAllTasks;
    private final LiveData<List<Task>> mAllTasksSortedByTimeStampDesc;
    private final LiveData<List<Task>> mAllTasksSortedByTimeStampAsc;
    private final LiveData<List<Task>> mAllTasksSortedByNameAsc;
    private final LiveData<List<Task>> mAllTasksSortedByNameDesc;

    // MediatorLiveData to aggregate the sorted tasks
    private MediatorLiveData<List<Task>> aggregatedTasks = new MediatorLiveData<>();
    private final LiveData<List<Project>> mAllProjects;

    // Enum for sorting preferences
    public enum SortingPreference {
        CREATION_TIMESTAMP_DESC,
        CREATION_TIMESTAMP_ASC,
        NAME_ASC,
        NAME_DESC,
        ALL_TASKS

    }



    public MainViewModel(@NonNull Application application) {
        super(application);
        mTodocRepository = TodocRepository.getInstance(application);
        mAllTasks = mTodocRepository.getAllTasks();
        mAllTasksSortedByTimeStampDesc = mTodocRepository.getAllTasksSortedByCreationTimestampDesc();
        mAllTasksSortedByTimeStampAsc = mTodocRepository.getAllTasksSortedByCreationTimestampAsc();
        mAllTasksSortedByNameAsc = mTodocRepository.getAllTasksSortedByNameAsc();
        mAllTasksSortedByNameDesc = mTodocRepository.getAllTasksSortedByNameDesc();
        mAllProjects = mTodocRepository.getAllProjects();

        // Set the initial source for aggregatedTasks (default sorting option)
        setSortingPreference(SortingPreference.ALL_TASKS);
    }

    public void setSortingPreference(SortingPreference sortingPreference) {
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


    public LiveData<List<Task>> getAggregatedTasks() {
        return aggregatedTasks;
    }

    LiveData<List<Task>> getAllTasks(){ return mAllTasks;}

    LiveData<List<Project>> getAllProjects() { return mAllProjects;}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}

    public Project getProjectFromId(long projectId) {
        return mTodocRepository.getProjectById(projectId);
    }

}
