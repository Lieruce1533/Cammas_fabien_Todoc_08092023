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

import java.util.List;

public class TodocRepository {

    // Singleton instance
    private static TodocRepository sInstance;
    private final TaskDao mTaskDao;
    private final LiveData<List<Project>> mAllProjects;
    private String preference;
    private final MutableLiveData<List<Task>> taskListFiltered = new MutableLiveData<>();



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
     * Live data who gets the value of one of the live data from the database
     * @param preference
     *
     */

    public void setPreference(String preference){
        this.preference= preference;
    }

    public LiveData<List<Task>> getTaskToDisplay(){

        switch (preference){
            case "Alphabetical":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical");
                mTaskDao.getTasksOrderedByProjectName().observeForever(new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        taskListFiltered.setValue(tasks);
                    }
                });

                break;

            case "Alphabetical_Inverted":
                Log.d("TAG repository", "getTaskToDisplay: Alphabetical_Inverted");
                mTaskDao.getTasksOrderedByProjectNameDesc().observeForever(new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        taskListFiltered.setValue(tasks);
                    }
                });


                break;

            case "Old_First":
                Log.d("TAG repository", "getTaskToDisplay: Old_First");
                mTaskDao.getAllTasksSortedByCreationTimestampDesc().observeForever(new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        taskListFiltered.setValue(tasks);
                    }
                });

                break;
            case "Recent_first":
                Log.d("TAG repository", "getTaskToDisplay: Recent_first");
                mTaskDao.getAllTasksSortedByCreationTimestampAsc().observeForever(new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        taskListFiltered.setValue(tasks);
                    }
                });
                break;
            default:
                Log.d("TAG repository", "getTaskToDisplay: default, all task");
                mTaskDao.getAllTasks().observeForever(new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        taskListFiltered.setValue(tasks);
                    }
                });
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


