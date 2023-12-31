package com.cleanup.todoc.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import java.util.Comparator;

/**
 * Model for the tasks of the application
 *
 */
@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = Project.class,
                                    parentColumns = "id",
                                    childColumns = "project_id"),
        indices = {@Index("project_id")})

@TypeConverters(ProjectConverter.class) // Use ProjectConverter for type conversion

public class Task {
    /**
     * The unique identifier of the task
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long id;


    @Ignore
    private long projectId; // This field is used as a foreign key

    @ColumnInfo(name = "project_id")
    private Project project;

     /**
     * The name of the task
     */
    // Suppress warning because setName is called in constructor
    //@SuppressWarnings("NullableProblems")
    @NonNull
    @ColumnInfo (name = "task_name")
    private String name;

    /**
     * The timestamp when the task has been created
     */
    @ColumnInfo(name= "creation_time_stamp")
    private long creationTimestamp;

    @Ignore
    public Task() {
    }

    /**
     * Instantiates a new Task.
     *

     * @param name              the name of the task to set
     * @param creationTimestamp the timestamp when the task has been created to set
     */

    public Task(Project project, @NonNull String name, long creationTimestamp) {
        this.project = project;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }
    /**
     * Returns the unique identifier of the task.
     *
     * @return the unique identifier of the task
     */
    public long getId() {
        return id;
    }
    /**
     * Sets the unique identifier of the task.
     *
     * @param id the unique identifier of the task to set
     */
    public void setId(long id) {
        this.id = id;
    }


    public long getProjectId() {
        return projectId;
    }
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }


     /**
     * Returns the name of the task.
     *
     * @return the name of the task
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the task.
     *
     * @param name the name of the task to set
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     *
     * @return the creationTimestamp of the task
     */
    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Sets the timestamp when the task has been created.
     *
     * @param creationTimestamp the timestamp when the task has been created to set
     */
    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
