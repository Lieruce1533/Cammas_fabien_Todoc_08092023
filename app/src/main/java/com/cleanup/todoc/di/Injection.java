package com.cleanup.todoc.di;

import android.content.Context;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TaskRoomDatabase;
import com.cleanup.todoc.repository.TodocRepository;

public class Injection {

    public static TodocRepository getsTodocRepository(TaskDao taskDao, ProjectDao projectDao) {
        return new TodocRepository(taskDao, projectDao);
    }

    public static TaskRoomDatabase getsTaskRoomDatabase(Context context) {
        return TaskRoomDatabase.getDatabase(context);
    }
}
