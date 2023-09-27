package com.cleanup.todoc.model;



import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

public class TaskProjectRelation {
    @Embedded
    public Project project;

    @Relation(
            parentColumn = "project_id",
            entityColumn = "id"
    )
    public List<Task> tasksAssociated;

}