package com.cleanup.todoc.model;

import static com.cleanup.todoc.model.ProjectConverterTest.MOCK_PROJECTS;


import java.util.List;

public class MockProjectConverter extends ProjectConverter {



    public static long idFromProject(Project project) {
        if (project==null) {
            return(0);
        }
        return(project.getId());
    }


    public static Project projectFromId(long id) {
        List<Project> projects = getProjects();
        for (Project project : projects) {
            if (project.getId() == id)
                return project;
        }
        return null;
    }

    static List<Project> getProjects() {
        return MOCK_PROJECTS;
    };


}
