package com.cleanup.todoc.database;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.LiveDataTestUtil;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DaoTests extends TestCase {

    private static final Project project1 = new Project(1L, "Projet Tartampion", 0xFFEADAD1) ;
    private static final Project project2 = new Project(2L, "Projet Lucidia", 0xFFB4CDBA);
    private static final Project project3 = new Project(3L, "Projet Circus", 0xFFA3CED2);

    private static final Task task1 = new Task((project2),"Test Task 1", 201);
    private static final Task task2 = new Task((project1),"Test Task 2", 256);
    private static final Task task3 = new Task((project3),"Test Task 3", 305);



    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskRoomDatabase appDatabase;
    private TaskDao taskDao;
    private ProjectDao projectDao;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room
                .inMemoryDatabaseBuilder(context, TaskRoomDatabase.class)
                .build();
        taskDao = appDatabase.mTaskDao();
        projectDao = appDatabase.mProjectDao();

    }
    @After
    public void closeDb() {
        appDatabase.close();
    }


    @Test
    public void insertions() throws InterruptedException {
        //ProjectDao inserts test
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        List<Project> projectsFromDao = LiveDataTestUtil.getValue(projectDao.getAllLiveProjects());
        assertEquals(0,projectsFromDao.size());
        projectDao.insertAll(projects);
        projectsFromDao = LiveDataTestUtil.getValue(projectDao.getAllLiveProjects());
        assertEquals(3,projectsFromDao.size());

        //TaskDao Insertions Test

        List<Task> tasksFromDao = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(0, tasksFromDao.size());
        taskDao.insert(task2);
        tasksFromDao = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(1, tasksFromDao.size());

    }

    @Test
    public void deleteTaskInTable() throws InterruptedException {
        // filling up database
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        List<Task> tasksFromDao = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(2, tasksFromDao.size());

        taskDao.deleteTaskInTable(tasksFromDao.get(1));


        tasksFromDao = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(1, tasksFromDao.size());
        assertEquals(task1.getName(), tasksFromDao.get(0).getName());
    }

    @Test
    public void getAllTasks() throws InterruptedException {
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> tasks = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test Task 1", tasks.get(0).getName());
        assertEquals("Test Task 2", tasks.get(1).getName());
        assertEquals("Test Task 3", tasks.get(2).getName());
    }


    @Test
    public void getAllTasksSortedByCreationTimestampDesc() throws InterruptedException {
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> tasks = LiveDataTestUtil.getValue(taskDao.getAllTasksSortedByCreationTimestampDesc());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test Task 3", tasks.get(0).getName());
        assertEquals("Test Task 2", tasks.get(1).getName());
        assertEquals("Test Task 1", tasks.get(2).getName());
    }

    @Test
    public void getAllTasksSortedByCreationTimestampAsc() throws InterruptedException {
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> tasks = LiveDataTestUtil.getValue(taskDao.getAllTasksSortedByCreationTimestampAsc());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test Task 1", tasks.get(0).getName());
        assertEquals("Test Task 2", tasks.get(1).getName());
        assertEquals("Test Task 3", tasks.get(2).getName());
    }

    @Test
    public void getTasksOrderedByProjectName() throws InterruptedException {
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> tasks = LiveDataTestUtil.getValue(taskDao.getTasksOrderedByProjectName());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test Task 3", tasks.get(0).getName());
        assertEquals("Test Task 1", tasks.get(1).getName());
        assertEquals("Test Task 2", tasks.get(2).getName());
    }

    @Test
    public void getTasksOrderedByProjectNameDesc() throws InterruptedException {
        List<Project> projects = new ArrayList<Project>();
        projects.add(project1);
        projects.add(project2);
        projects.add(project3);
        projectDao.insertAll(projects);
        taskDao.insert(task1);
        taskDao.insert(task2);
        taskDao.insert(task3);
        List<Task> tasks = LiveDataTestUtil.getValue(taskDao.getTasksOrderedByProjectNameDesc());
        assertNotNull(tasks);
        assertEquals(3, tasks.size());
        assertEquals("Test Task 2", tasks.get(0).getName());
        assertEquals("Test Task 1", tasks.get(1).getName());
        assertEquals("Test Task 3", tasks.get(2).getName());
    }
}