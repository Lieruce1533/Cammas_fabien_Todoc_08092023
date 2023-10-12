package com.cleanup.todoc.Repository;

import android.app.Application;


import androidx.lifecycle.LiveData;


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
     * Live data's before preferences
     *
     */
    public LiveData<List<Task>> getAllTasks(){
        return mTaskDao.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasksSortedByCreationTimestampDesc(){
        return mTaskDao.getAllTasksSortedByCreationTimestampDesc();
    }
    public LiveData<List<Task>> getAllTasksSortedByCreationTimestampAsc(){
        return mTaskDao.getAllTasksSortedByCreationTimestampAsc();
    }
    public LiveData<List<Task>> getTasksOrderedByProjectName(){
        return mTaskDao.getTasksOrderedByProjectName();
    }
    public LiveData<List<Task>> getTasksOrderedByProjectNameDesc(){
        return mTaskDao.getTasksOrderedByProjectNameDesc();
    }


    /**
     * Projects methods
     * @return
     */
    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }

    }


