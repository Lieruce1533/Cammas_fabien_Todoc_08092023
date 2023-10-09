package com.cleanup.todoc;


import static org.junit.Assert.assertEquals;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabaseUnitTest {

    private TaskRoomDatabase mTaskRoomDatabase;
    private TaskDao mTaskDao;
    private ProjectDao mProjectDao;


    @Before
    public void setUp() {
        mTaskRoomDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), TaskRoomDatabase.class).build();
        mProjectDao = mTaskRoomDatabase.mProjectDao();
        mTaskDao = mTaskRoomDatabase.mTaskDao();
    }
    @After
    public void tearDown() {
        mTaskRoomDatabase.close();
    }

    @Test
    public void insertTaskIsWorking() {
        Task mtask = new Task();
        Project mProject = mProjectDao.getProjectById(1);
        mtask.setProject(mProject);
        mtask.setName("Test Task");
        mtask.setCreationTimestamp(System.currentTimeMillis());
        mTaskDao.insert(mtask);
        List<Task> tasks = mTaskDao.getAllTasks().getValue();
        assertEquals(2, tasks.size());
        assertEquals(mtask, tasks.get(1));
    }

    @Test
    public void deleteTaskIsWorking() {
        Task mtask = new Task();
        mtask.setProject(mProjectDao.getProjectById(1));
        mtask.setName("new Task");
        mtask.setCreationTimestamp(System.currentTimeMillis());
        mTaskDao.insert(mtask);
        mTaskDao.deleteTaskInTable(mtask);
        List<Task> tasks = mTaskDao.getAllTasks().getValue();
        assertEquals(0, tasks.size());
    }
    /*@Test
    public void getAllTasksIsWorking() {

    }*/

}
