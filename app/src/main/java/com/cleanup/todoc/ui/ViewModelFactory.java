package com.cleanup.todoc.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.repository.TodocRepository;
import com.cleanup.todoc.database.TaskRoomDatabase;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    private final TodocRepository mTodocRepository;

    private ViewModelFactory() {
        Context context = MainApplication.getApplication().getApplicationContext();
        TaskRoomDatabase appDatabase = TaskRoomDatabase.getDatabase(context);
        mTodocRepository = new TodocRepository(appDatabase.mTaskDao(), appDatabase.mProjectDao());

    }

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(mTodocRepository);
        }
        // Handle other view models if needed
        throw new IllegalArgumentException("Unknown ViewModel class");
    }



}