package com.cleanup.todoc.model;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.cleanup.todoc.database.ProjectDao;

import java.util.Arrays;
import java.util.List;

@ProvidedTypeConverter
public class ProjectConverter {

    private static ProjectDao projectDaoInstance;

    public static void initialize(ProjectDao projectDao) {
        projectDaoInstance = projectDao;
    }


    @TypeConverter
    public static long idFromProject(Project project) {
        if (project==null) {
            return(0);
        }
        return(project.getId());
    }

    @TypeConverter
    public static Project projectFromId(long id) {

        return projectDaoInstance.getProjectById(id);
    }

    static List<Project> getProjects() {
        return projectDaoInstance.getAllProjects();
    };
}
