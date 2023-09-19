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

    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;
    private final TaskRoomDatabase mTaskRoomDatabase;
    private LiveData<List<Task>> mAllTasks;
    private LiveData<List<Project>> mAllProjects;
    private Project project;


    public TodocRepository(Application application) {

        mTaskRoomDatabase = TaskRoomDatabase.getDatabase(application.getApplicationContext());
        mTaskDao = mTaskRoomDatabase.mTaskDao();
        mProjectDao = mTaskRoomDatabase.mProjectDao();
        mAllTasks = mTaskDao.getAllTasks();
        mAllProjects = mProjectDao.getAllLiveProjects();

    }

    // Create and return the Singleton instance
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
     * Tasks methods
     * @param task
     */
    public void insert(Task task){
        mTaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.insert(task);
        } );
    }
    public void delete(Task task){
        mTaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            mTaskDao.deleteTaskInTable(task);
        } );
    }
    public LiveData<List<Task>> getAllTasks(){
        return mAllTasks;
    }

    /**
     * Projects methods
     * @return
     */

    public Project getProjectById(Long projectId){
       mTaskRoomDatabase.databaseWriteExecutor.execute(() -> {
            mProjectDao.getProjectById(projectId);
        } );
        return project;
    }
    public LiveData<List<Project>> getAllProjects() {
        return mAllProjects;
    }

    }


