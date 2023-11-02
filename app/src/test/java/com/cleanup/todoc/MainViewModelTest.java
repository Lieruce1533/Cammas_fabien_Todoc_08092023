package com.cleanup.todoc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repository.TodocRepository;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.ui.MainViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private TodocRepository repository;

    private MainViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize your ViewModel with the mock repository
        viewModel = new MainViewModel(repository);

        try {
            // Use reflection to access the private field
            Field aggregatedTasksField = viewModel.getClass().getDeclaredField("aggregatedTasks");
            aggregatedTasksField.setAccessible(true);

            // Create a mock LiveData object with your test data
            MutableLiveData<List<Task>> mockLiveData = new MutableLiveData<>();
            List<Task> mockTasks = new ArrayList<>(); // Replace with your test data
            mockLiveData.setValue(mockTasks);

            // Set the aggregatedTasks field to the mock LiveData
            aggregatedTasksField.set(viewModel, mockLiveData);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHandleSortingPreference() {
        String preference = "Alphabetical";
        viewModel.handleSortingPreference(preference);
        verify(repository).onSortingTypeChanged(preference);
    }

    @Test
    public void testUpdateIsNullEmptyTasks() throws InterruptedException {

        // Set up a scenario with an empty list of tasks
        LiveData<List<Task>> tasksLiveData = new MutableLiveData<>();


        when(repository.getTasksLiveData()).thenReturn(tasksLiveData);

        // Trigger the updateIsNull method
        viewModel.updateIsNull();

        // Assert that isNull LiveData is set to true
        LiveData<Boolean> isNullLiveData = viewModel.getIsNull();
        assertTrue(LiveDataTestUtil.getValue(isNullLiveData)); // You may need to adjust this based on your LiveData testing approach
    }

    @Test
    public void testUpdateIsNullNonEmptyTasks() {
        // Set up a scenario with a non-empty list of tasks
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task((new Project(1, "Project 1", 0xFF6600)), "Task 1", 123));
        MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
        tasksLiveData.setValue(tasks);

        // Use reflection to set the aggregatedTasks field
        try {
            Field aggregatedTasksField = viewModel.getClass().getDeclaredField("aggregatedTasks");
            aggregatedTasksField.setAccessible(true);
            aggregatedTasksField.set(viewModel, tasksLiveData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewModel.updateIsNull();
        LiveData<Boolean> isNullLiveData = viewModel.getIsNull();

        // Check that isNullLiveData is set to false
        assertNotNull(isNullLiveData.getValue());
        assertFalse(isNullLiveData.getValue());
    }

    @Test
    public void testGetAggregatedTasks() {
        // Create a list of tasks for testing
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task((new Project(1, "Project 1", 0xFF6600)), "Task 1", 123));
        MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
        tasksLiveData.setValue(tasks);

        // Mock the repository to return the tasksLiveData
        when(repository.getTasksLiveData()).thenReturn(tasksLiveData);

        // Call the getAggregatedTasks method
        LiveData<List<Task>> aggregatedTasks = viewModel.getAggregatedTasks();

        // Assert that aggregatedTasks is not null
        assertNotNull(aggregatedTasks);

    }

    @Test
    public void testGetAllProjects() {
        // Create a list of projects for testing
        List<Project> projects = new ArrayList<>();
        projects.add(new Project(1, "Project 1", 0xFF6600));
        MutableLiveData<List<Project>> projectsLiveData = new MutableLiveData<>();
        projectsLiveData.setValue(projects);

        // Mock the repository to return the projectsLiveData
        when(repository.getAllProjects()).thenReturn(projectsLiveData);

        // Call the getAllProjects method
        LiveData<List<Project>> allProjects = viewModel.getAllProjects();

        // Assert that allProjects is not null
        assertNotNull(allProjects);
    }

    @Test
    public void testInsertTask() {
        // Create a task for testing
        Project project = new Project(1, "Project 1", 0xFF6600);
        Task task = new Task(project, "Task 1", 123);

        // Call the insert method
        viewModel.insert(task);

        // Verify that the repository's insert method is called with the task
        verify(repository).insert(task);
    }

    @Test
    public void testDeleteTask() {
        // Create a task for testing
        Project project = new Project(1, "Project 1", 0xFF6600);
        Task task = new Task(project, "Task 1", 123);

        // Call the delete method
        viewModel.delete(task);

        // Verify that the repository's delete method is called with the task
        verify(repository).delete(task);
    }
}
