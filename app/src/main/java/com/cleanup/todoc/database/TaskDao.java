package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.cleanup.todoc.model.Task;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;


@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);
    @Delete(entity = Task.class)
    void deleteTaskInTable(Task task);

    /**
     * sorting Tasks here
     *
     */

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getAllTasks();
    @Query("SELECT * FROM tasks_table ORDER BY creation_time_stamp DESC")
    LiveData<List<Task>> getAllTasksSortedByCreationTimestampDesc();
    @Query("SELECT * FROM tasks_table ORDER BY creation_time_stamp ASC")
    LiveData<List<Task>> getAllTasksSortedByCreationTimestampAsc();
    @Query("SELECT tasks_table.* FROM tasks_table INNER JOIN projects ON tasks_table.project_id = projects.id ORDER BY projects.name ASC")
    LiveData<List<Task>> getTasksOrderedByProjectName();
    @Query("SELECT tasks_table.* FROM tasks_table INNER JOIN projects ON tasks_table.project_id = projects.id ORDER BY projects.name DESC")
    LiveData<List<Task>> getTasksOrderedByProjectNameDesc();



}
