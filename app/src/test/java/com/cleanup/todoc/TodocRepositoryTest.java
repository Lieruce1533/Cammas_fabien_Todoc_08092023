package com.cleanup.todoc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



import androidx.lifecycle.LiveData;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import androidx.room.Room;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.TodocRepository;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TodocRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private TaskDao taskDao;
    @Mock
    private ProjectDao projectDao;
    private TodocRepository repository;

    @Before
    public void setup() {
        repository = new TodocRepository(taskDao, projectDao);
    }

    @Test
    public void testInsertTask() {
        Project projet1 = projectDao.getProjectById(1);
        Task task = new Task(projet1, "aaa", 123);

        repository.insert(task);

        // Wait for LiveData operation to complete
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();
        LiveDataTestUtil.observeForTesting(tasksLiveData, tasks -> {
            // Assert that the inserted task is present in the list
            assertTrue(tasks.contains(task));
        });
        verify(taskDao).insert(task);

    }


    @Test
    public void testDeleteTask() {
        Project projet1 = projectDao.getProjectById(1);
        Task task = new Task(projet1, "aaa", 123);

        repository.delete(task);
        // Wait for LiveData operation to complete
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();
        LiveDataTestUtil.observeForTesting(tasksLiveData, tasks -> {
            // Assert that the deleted task is not present in the list
            assertFalse(tasks.contains(task));
        });

        verify(taskDao).deleteTaskInTable(task);
    }

    /**
     * Test if the sorting method calls the right query method in the Dao:
     */
    @Test
    public void testGetTasksLiveDataAlphabetical() {

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
        List<Task> mockTasks = createMockTasks(); // Create some mock tasks

        // Mock the DAO to return the mockLiveData
        when(taskDao.getTasksOrderedByProjectName()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        repository.onSortingTypeChanged("Alphabetical");

        // Now, you can observe the LiveData and assert its value
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();

        tasksLiveData.observeForever(tasks -> {
            // Assert that the LiveData contains the expected tasks
            assertTrue(tasks.containsAll(mockTasks));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockTasks);
        verify(taskDao).getTasksOrderedByProjectName();
    }

    @Test
    public void testGetTasksLiveDataInvertedAlphabetical() {

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
        List<Task> mockTasks = createMockTasks(); // Create some mock tasks

        // Mock the DAO to return the mockLiveData
        when(taskDao.getTasksOrderedByProjectNameDesc()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        repository.onSortingTypeChanged("Alphabetical_Inverted");

        // Now, you can observe the LiveData and assert its value
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();

        tasksLiveData.observeForever(tasks -> {
            // Assert that the LiveData contains the expected tasks
            assertTrue(tasks.containsAll(mockTasks));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockTasks);
        verify(taskDao).getTasksOrderedByProjectNameDesc();
    }
    @Test
    public void testGetTasksLiveDataOldFirst() {

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
        List<Task> mockTasks = createMockTasks(); // Create some mock tasks

        // Mock the DAO to return the mockLiveData
        when(taskDao.getAllTasksSortedByCreationTimestampDesc()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        repository.onSortingTypeChanged("Old_First");

        // Now, you can observe the LiveData and assert its value
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();

        tasksLiveData.observeForever(tasks -> {
            // Assert that the LiveData contains the expected tasks
            assertTrue(tasks.containsAll(mockTasks));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockTasks);
        verify(taskDao).getAllTasksSortedByCreationTimestampDesc();
    }

    @Test
    public void testGetTasksLiveDataRecentFirst() {

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
        List<Task> mockTasks = createMockTasks(); // Create some mock tasks

        // Mock the DAO to return the mockLiveData
        when(taskDao.getAllTasksSortedByCreationTimestampAsc()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        repository.onSortingTypeChanged("Recent_First");

        // Now, you can observe the LiveData and assert its value
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();

        tasksLiveData.observeForever(tasks -> {
            // Assert that the LiveData contains the expected tasks
            assertTrue(tasks.containsAll(mockTasks));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockTasks);
        verify(taskDao).getAllTasksSortedByCreationTimestampAsc();
    }
    @Test
    public void testGetTasksLiveDataDefault() {

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
        List<Task> mockTasks = createMockTasks(); // Create some mock tasks

        // Mock the DAO to return the mockLiveData
        when(taskDao.getAllTasks()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        repository.onSortingTypeChanged("all tasks");

        // Now, you can observe the LiveData and assert its value
        LiveData<List<Task>> tasksLiveData = repository.getTasksLiveData();

        tasksLiveData.observeForever(tasks -> {
            // Assert that the LiveData contains the expected tasks
            assertTrue(tasks.containsAll(mockTasks));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockTasks);
        verify(taskDao).getAllTasks();
    }

    private List<Task> createMockTasks() {
        // Create and return a list of mock tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(new Project(1, "Project 1", 0xFF6600), "Task 1", 123));
        tasks.add(new Task(new Project(2, "Project 2", 0xFF6600), "Task 2", 124));
        tasks.add(new Task(new Project(3, "Project 3", 0xFF6600), "Task 3", 125));
        tasks.add(new Task(new Project(2, "Project 2", 0xFF6600), "Task 4", 126));
        // Add more tasks as needed
        return tasks;
    }

    @Test
    public void testGetAllProjects() {
        // Create a list of mock projects
        List<Project> mockProjects = createMockProjects();

        // Create a mock LiveData that will be returned by the DAO
        MutableLiveData<List<Project>> mockLiveData = new MutableLiveData<>();

        // Mock the DAO to return the mockLiveData
        when(projectDao.getAllLiveProjects()).thenReturn(mockLiveData);

        // Trigger the method that updates the LiveData
        LiveData<List<Project>> projectsLiveData = repository.getAllProjects();

        // Now, you can observe the LiveData and assert its value
        projectsLiveData.observeForever(projects -> {
            // Assert that the LiveData contains the expected projects
            assertTrue(projects.containsAll(mockProjects));
        });

        // Set the value of the LiveData (this simulates the database returning data)
        mockLiveData.setValue(mockProjects);

        // Verify that the DAO method was called
        verify(projectDao).getAllLiveProjects();
    }
    private List<Project> createMockProjects(){
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
        projects.add(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
        projects.add(new Project(3L, "Projet Circus", 0xFFA3CED2));
        return projects;
    }

}
