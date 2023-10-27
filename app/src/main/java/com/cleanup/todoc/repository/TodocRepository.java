package com.cleanup.todoc.repository;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;


import java.util.List;

public class TodocRepository {


    @NonNull
    private  final TaskDao mTaskDao;
    @NonNull
    private  final ProjectDao mProjectDao;

    private final MutableLiveData<String> sortingTypeMutableLiveData = new MutableLiveData<>("All tasks");


    public TodocRepository(@NonNull TaskDao mTaskDao, @NonNull ProjectDao mProjectDao) {
        this.mTaskDao = mTaskDao;
        this.mProjectDao = mProjectDao;
    }
    /**
     * Task method Insertion
     * @param task
     */
    public void insert(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        } );
    }
    /**
     * Task method deletion
     * @param task
     */
    public void delete(Task task){
        TaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.deleteTaskInTable(task);
        } );
    }
    /**
     * Live data who gets the value of one of the live data from the database
     * @param preference
     *
     */
    public void onSortingTypeChanged(String preference) {
        sortingTypeMutableLiveData.setValue(preference);
    }

    public LiveData<List<Task>> getTasksLiveData() {
        return Transformations.switchMap(sortingTypeMutableLiveData, preference -> {
            switch (preference) {
                case "Alphabetical":

                    return mTaskDao.getTasksOrderedByProjectName();
                case "Alphabetical_Inverted":

                    return mTaskDao.getTasksOrderedByProjectNameDesc();
                case "Recent_First":

                    return mTaskDao.getAllTasksSortedByCreationTimestampDesc();
                case "Old_First":

                    return mTaskDao.getAllTasksSortedByCreationTimestampAsc();
                default:

                    return mTaskDao.getAllTasks();
            }
        });
    }
    /**
     * Projects methods
     * @return
     */
    public LiveData<List<Project>> getAllProjects(){
        return mProjectDao.getAllLiveProjects();
    }


}


