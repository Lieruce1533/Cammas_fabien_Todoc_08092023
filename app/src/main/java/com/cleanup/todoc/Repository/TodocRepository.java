package com.cleanup.todoc.Repository;

import android.app.Application;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TodocRepository {

    // Singleton instance
    private static TodocRepository sInstance;
    private final TaskDao mTaskDao;
    private final LiveData<List<Project>> mAllProjects;
    private LiveData<List<Task>> tasksSortedAlphabetically;
    private LiveData<List<Task>> tasksSortedAlphabeticallyInverted;
    private LiveData<List<Task>> tasksSortedOldFirst;
    private LiveData<List<Task>> tasksSortedRecentFirst;
    private LiveData<List<Task>> allTasks;
    //private String preference;





    public TodocRepository(Application application) {

        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application.getApplicationContext());
        mTaskDao = taskRoomDatabase.mTaskDao();
        ProjectDao projectDao = taskRoomDatabase.mProjectDao();
        mAllProjects = projectDao.getAllLiveProjects();

        tasksSortedAlphabetically = mTaskDao.getTasksOrderedByProjectName();
        tasksSortedAlphabeticallyInverted = mTaskDao.getTasksOrderedByProjectNameDesc();
        tasksSortedOldFirst = mTaskDao.getAllTasksSortedByCreationTimestampDesc();
        tasksSortedRecentFirst = mTaskDao.getAllTasksSortedByCreationTimestampAsc();
        allTasks = mTaskDao.getAllTasks();


    }

    /**
     * Create and return the Singleton instance
     */
    public static TodocRepository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (TodocRepository.class) {
                if (sInstance == null) {
                    sInstance = new TodocRepository(application);
                }
            }
        }
        return sInstance;
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


    public LiveData<List<Task>> getTaskToDisplay(String preference) {

        switch (preference) {
            case "Alphabetical":
                Log.d("TAG repository", "getTaskToDisplay: alphabetical ");
                return tasksSortedAlphabetically;

            case "Alphabetical_Inverted":
                Log.d("TAG repository", "getTaskToDisplay: alphabetical inverted ");
                return tasksSortedAlphabeticallyInverted;

            case "Old_First":
                Log.d("TAG repository", "getTaskToDisplay: old first ");
                return tasksSortedOldFirst;

            case "Recent_first":
                Log.d("TAG repository", "getTaskToDisplay: recent first");
                return tasksSortedRecentFirst;

            default:
                Log.d("TAG repository", "getTaskToDisplay: All tasks ");
                return allTasks;
        }

    }


    /*
    public LiveData<List<Task>> getTaskToDisplay( String preference){


        if (preference.equals("Alphabetical")){
            Log.d("TAG repository", "getTaskToDisplay: alphabetical ");
            return tasksSortedAlphabetically;
        }else if (preference.equals("Alphabetical_Inverted")){
            Log.d("TAG repository", "getTaskToDisplay: alphabetical inverted ");
            return tasksSortedAlphabeticallyInverted;
        }else if (preference.equals("Old_First")){
            Log.d("TAG repository", "getTaskToDisplay: old first ");
            return tasksSortedOldFirst;
        }else if (preference.equals("Recent_first")){
            Log.d("TAG repository", "getTaskToDisplay: recent first");
            return tasksSortedRecentFirst;
        } else{
            Log.d("TAG repository", "getTaskToDisplay: All tasks ");
            return mTaskDao.getAllTasks();
        }
    }*/



    /**
     * Projects methods
     * @return
     */
    public LiveData<List<Project>> getAllProjects(){
        return mAllProjects;
    }


}


