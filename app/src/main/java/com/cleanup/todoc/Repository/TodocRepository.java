package com.cleanup.todoc.Repository;

import android.app.Application;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TodocRepository {

    // Singleton instance
    private static TodocRepository sInstance;
    private final TaskDao mTaskDao;
    private final LiveData<List<Project>> mAllProjects;





    public TodocRepository(Application application) {

        TaskRoomDatabase taskRoomDatabase = TaskRoomDatabase.getDatabase(application.getApplicationContext());
        mTaskDao = taskRoomDatabase.mTaskDao();
        ProjectDao projectDao = taskRoomDatabase.mProjectDao();
        mAllProjects = projectDao.getAllLiveProjects();
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
//==============================================================================================
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
//==============================================================================================

//==============================================================================================
    /**
     * Live data who gets the value of one of the live data from the database
     * @param preference
     *
     */

    public LiveData<List<Task>> getTaskToDisplay(String preference){

        LiveData<List<Task>> taskListFiltered;

        switch (preference){
            case "Alphabetical":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical");

                taskListFiltered = mTaskDao.getTasksOrderedByProjectName();
                break;

            case "Alphabetical_Inverted":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical_Inverted");

                taskListFiltered= mTaskDao.getTasksOrderedByProjectNameDesc();
                break;

            case "Old_First":
                Log.d("TAG repository", "getTaskToDisplay: Old_First");

                taskListFiltered = mTaskDao.getAllTasksSortedByCreationTimestampDesc();
                break;
            case "Recent_first":
                Log.d("TAG repository", "getTaskToDisplay: Recent_first");

                taskListFiltered = mTaskDao.getAllTasksSortedByCreationTimestampAsc();
                break;
            default:
                Log.d("TAG repository", "getTaskToDisplay: default, all task");

                taskListFiltered = mTaskDao.getAllTasks();
                break;
        }
        Log.d("TAG repository", "getTaskToDisplay: before return");
        return taskListFiltered;


    }




    /**
     * Projects methods
     * @return
     */
    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }

    }


