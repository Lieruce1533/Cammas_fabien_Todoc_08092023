package com.cleanup.todoc.model;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;

@ProvidedTypeConverter
public class ProjectConverter {



    @TypeConverter
    public static long idFromProject(Project project) {
        if (project==null) {
            return(0);
        }
        return(project.getId());
    }

    @TypeConverter
    public static Project projectFromId(long id) {
        List<Project> projects = getProjects();
        for (Project project : projects) {
            if (project.getId() == id)
                return project;
        }
        return null;
    }

    static List<Project> getProjects() {
        return Arrays.asList(Project.getAllProjects());
    };
}
