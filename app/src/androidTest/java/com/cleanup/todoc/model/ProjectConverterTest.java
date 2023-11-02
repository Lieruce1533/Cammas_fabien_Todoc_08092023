package com.cleanup.todoc.model;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.List;
@RunWith(AndroidJUnit4.class)
public class ProjectConverterTest extends TestCase {
    // Create a mock list of projects for testing
    static final List<Project> MOCK_PROJECTS = new ArrayList<>();


    @Before
    public void setup() {
        MOCK_PROJECTS.add(new Project(1, "Project 1", 0xFFEADAD1));
        MOCK_PROJECTS.add(new Project(2, "Project 2", 0xFFB4CDBA));
        MOCK_PROJECTS.add(new Project(3, "Project 3", 0xFFA3CED2));


    }
    @Test
    public void testIdFromProject() {
        ProjectConverter converter = new ProjectConverter();
        // Test conversion from Project to long
        long projectId = converter.idFromProject(MOCK_PROJECTS.get(0));
        assertEquals(1, projectId);
        // Test conversion from null Project
        long nullProjectId = converter.idFromProject(null);
        assertEquals(0, nullProjectId);
    }

    @Test
    public void testProjectFromId() {
        MockProjectConverter converter = new MockProjectConverter();

        // Test conversion from long to Project
        Project project = converter.projectFromId(2);
        assertNotNull(project);
        assertEquals("Project 2", project.getName());
        assertEquals(0xFFB4CDBA, project.getColor());
        // Test conversion from an unknown id
        Project unknownProject = converter.projectFromId(4);
        assertNull(unknownProject);
    }
}