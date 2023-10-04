package com.cleanup.todoc;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {

    ProjectDao mProjectDao;

    Project projet1 = mProjectDao.getProjectById(1);
    Project projet2 = mProjectDao.getProjectById(2);
    Project projet3 = mProjectDao.getProjectById(3);
    Project projet4 = mProjectDao.getProjectById(4);
    @Test
    public void test_projects() {
        final Task task1 = new Task(projet1, "task 1", new Date().getTime());
        final Task task2 = new Task(projet2, "task 2", new Date().getTime());
        final Task task3 = new Task(projet3, "task 3", new Date().getTime());
        final Task task4 = new Task(projet4, "task 4", new Date().getTime());

        assertEquals("Projet Tartampion", task1.getProject().getName());
        assertEquals("Projet Lucidia", task2.getProject().getName());
        assertEquals("Projet Circus", task3.getProject().getName());
        assertNull(task4.getProject());
    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(projet1, "aaa", 123);
        final Task task2 = new Task(projet2, "zzz", 124);
        final Task task3 = new Task(projet3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(projet1, "aaa", 123);
        final Task task2 = new Task(projet2, "zzz", 124);
        final Task task3 = new Task(projet3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(projet1, "aaa", 123);
        final Task task2 = new Task(projet2, "zzz", 124);
        final Task task3 = new Task(projet3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(projet1, "aaa", 123);
        final Task task2 = new Task(projet2, "zzz", 124);
        final Task task3 = new Task(projet3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}