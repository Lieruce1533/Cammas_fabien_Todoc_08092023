package com.cleanup.todoc.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.ProjectConverter;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.model.ProjectWithTaskRelation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
@TypeConverters(ProjectConverter.class)
public abstract class TaskRoomDatabase extends RoomDatabase {

    public abstract TaskDao mTaskDao();
    public abstract ProjectDao mProjectDao();
    private static volatile TaskRoomDatabase INSTANCE;
    private static Context appContext;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    public static TaskRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    appContext = context.getApplicationContext();
                    INSTANCE = Room.databaseBuilder(appContext,
                                    TaskRoomDatabase.class, "tasks")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                    }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                ProjectDao pDao = INSTANCE.mProjectDao();
                List<Project> projects = readProjectsFromJson(appContext);
                pDao.insertAll(projects);
                TaskDao tDao = INSTANCE.mTaskDao();
                Task mtask = new Task();
                mtask.setProject(pDao.getProjectById(1));
                mtask.setName("new Task");
                mtask.setCreationTimestamp(System.currentTimeMillis());
                tDao.insert(mtask);

            });
        }
    };

    private static List<Project> readProjectsFromJson(Context context) {
        List<Project> projects = new ArrayList<>();

        try {
            InputStream inputStream = context.getAssets().open("projects.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse the JSON array into Project objects
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long id = jsonObject.getLong("id");
                String name = jsonObject.getString("name");
                int color = jsonObject.getInt("color");
                projects.add(new Project(id, name, color));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return projects;
    }



}
