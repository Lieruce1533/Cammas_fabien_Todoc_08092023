package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.cleanup.todoc.model.Task;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);
    @Delete(entity = Task.class)
    void deleteTaskInTable(Task task);

    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();


    //sorting Tasks possible here
}
