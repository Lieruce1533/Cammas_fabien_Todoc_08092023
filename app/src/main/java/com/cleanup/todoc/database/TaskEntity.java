package com.cleanup.todoc.database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks_table", foreignKeys = @ForeignKey(entity = ProjectEntity.class,
                                                                parentColumns = "id",
                                                                childColumns = "ProjectId"))


public class TaskEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private long ProjectId;
    private String name;
    private long creationTimestamp;

    public TaskEntity(long id, long projectId, String name, long creationTimestamp) {
        this.id = id;
        ProjectId = projectId;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return ProjectId;
    }

    public void setProjectId(long projectId) {
        ProjectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}
