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

    private static TodocRepository mTodocRepository;

    // MediatorLiveData to aggregate the sorted tasks
    private static MediatorLiveData<List<Task>> aggregatedTasks = new MediatorLiveData<>();
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
        mAllProjects = mTodocRepository.getAllProjects();

        // Set the initial source for aggregatedTasks (default sorting option)
        setSortingPreference(SortingPreference.ALL_TASKS);
    }

    public static void setSortingPreference(SortingPreference sortingPreference) {
        switch (sortingPreference) {
            case CREATION_TIMESTAMP_DESC:
                aggregatedTasks.addSource(mTodocRepository.getAllTasksSortedByCreationTimestampDesc(), tasks -> aggregatedTasks.setValue(tasks));
                break;
            case CREATION_TIMESTAMP_ASC:
                aggregatedTasks.addSource(mTodocRepository.getAllTasksSortedByCreationTimestampAsc(), tasks -> aggregatedTasks.setValue(tasks));
                break;
            case NAME_ASC:
                aggregatedTasks.addSource(mTodocRepository.getTasksOrderedByProjectName(), tasks -> aggregatedTasks.setValue(tasks));
                break;
            case NAME_DESC:
                aggregatedTasks.addSource(mTodocRepository.getTasksOrderedByProjectNameDesc(), tasks -> aggregatedTasks.setValue(tasks));
                break;
            case ALL_TASKS:
                aggregatedTasks.addSource(mTodocRepository.getAllTasks(), tasks -> aggregatedTasks.setValue(tasks));
                break;
        }
    }


    public LiveData<List<Task>> getAggregatedTasks() {
        return aggregatedTasks;
    }

    LiveData<List<Project>> getAllProjects() { return mAllProjects;}

    public void insert(Task task) { mTodocRepository.insert(task);}
    public void delete(Task task) { mTodocRepository.delete(task);}



}
