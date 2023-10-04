package com.cleanup.todoc.model;



import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ProjectWithTaskRelation {
    @Embedded
    public Project project;

    @Relation(
            parentColumn = "project_id",
            entityColumn = "id"
    )
    public List<Task> tasksAssociated;

}