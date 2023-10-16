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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);
    @Delete(entity = Task.class)
    void deleteTaskInTable(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE project_id = :projectId")
    LiveData<List<Task>> getTasksByProject(int projectId);

    /**
     * sorting Tasks here
     */

    @Query("SELECT * FROM tasks ORDER BY creation_time_stamp DESC")
    LiveData<List<Task>> getAllTasksSortedByCreationTimestampDesc();
    @Query("SELECT * FROM tasks ORDER BY creation_time_stamp ASC")
    LiveData<List<Task>> getAllTasksSortedByCreationTimestampAsc();

    @Query("SELECT tasks.* FROM tasks INNER JOIN projects ON tasks.project_id = projects.id ORDER BY projects.name ASC")
    LiveData<List<Task>> getTasksOrderedByProjectName();
    @Query("SELECT tasks.* FROM tasks INNER JOIN projects ON tasks.project_id = projects.id ORDER BY projects.name DESC")
    LiveData<List<Task>> getTasksOrderedByProjectNameDesc();



}
