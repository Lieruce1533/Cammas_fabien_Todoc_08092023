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
    private static TaskDao mTaskDao;
    private final ProjectDao mProjectDao;
    private final TaskRoomDatabase mTaskRoomDatabase;
    private final LiveData<List<Project>> mAllProjects;





    public TodocRepository(Application application) {

        mTaskRoomDatabase = TaskRoomDatabase.getDatabase(application.getApplicationContext());
        mTaskDao = mTaskRoomDatabase.mTaskDao();
        mProjectDao = mTaskRoomDatabase.mProjectDao();
        mAllProjects = mProjectDao.getAllLiveProjects();
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
    /**
     * Live data's before preferences
     *
     */
    public static LiveData<List<Task>> getAllTasks(){
        Log.d("TAG access DAO", "getAllTasks: is solicited ");
        List<Task> tasks = mTaskDao.getAllTasks().getValue();
        Log.d("TAG access DAO", "Received tasks: " + tasks.size());
        return mTaskDao.getAllTasks();
    }
    public static LiveData<List<Task>> getAllTasksSortedByCreationTimestampDesc(){
        Log.d("TAG access DAO", "getAllTasksSortedByCreationTimestampDesc: is solicited ");
        return mTaskDao.getAllTasksSortedByCreationTimestampDesc();
    }
    public static LiveData<List<Task>> getAllTasksSortedByCreationTimestampAsc(){
        Log.d("TAG access DAO", "getAllTasksSortedByCreationTimestampAsc: is solicited ");
        return mTaskDao.getAllTasksSortedByCreationTimestampAsc();
    }
    public static LiveData<List<Task>> getTasksOrderedByProjectName(){
        Log.d("TAG access DAO", "getTasksOrderedByProjectName: is solicited ");
        return mTaskDao.getTasksOrderedByProjectName();
    }
    public static LiveData<List<Task>> getTasksOrderedByProjectNameDesc(){
        Log.d("TAG access DAO", "getTasksOrderedByProjectNameDesc: is solicited ");
        return mTaskDao.getTasksOrderedByProjectNameDesc();
    }
//==============================================================================================
    /**
     * Mutable live data who gets the value of one of the live data from the database
     * @param preference
     *
     */

    public static LiveData<List<Task>> getTaskToDisplay(String preference){

        MutableLiveData<List<Task>> taskListFiltered = new MutableLiveData<>();
        List<Task> tasks;
        switch (preference){
            case "Alphabetical":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical");
                tasks = getTasksOrderedByProjectName().getValue();
                taskListFiltered.setValue(tasks);
                break;

            case "Alphabetical_Inverted":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical_Inverted");
                tasks = getTasksOrderedByProjectNameDesc().getValue();
                taskListFiltered.setValue(tasks);
                break;

            case "Old_First":
                Log.d("TAG repository", "getTaskToDisplay: Old_First");
                tasks = getAllTasksSortedByCreationTimestampDesc().getValue();
                taskListFiltered.setValue(tasks);
                break;
            case "Recent_first":
                Log.d("TAG repository", "getTaskToDisplay: Recent_first");
                tasks = getAllTasksSortedByCreationTimestampAsc().getValue();
                taskListFiltered.setValue(tasks);
                break;
            default:
                Log.d("TAG repository", "getTaskToDisplay: default, all task");
                tasks = getAllTasks().getValue();
                taskListFiltered.setValue(tasks);
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


