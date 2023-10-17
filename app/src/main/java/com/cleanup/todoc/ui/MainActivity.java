package com.cleanup.todoc.ui;


import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.ActivityMainBinding;
import com.cleanup.todoc.databinding.DialogAddTaskBinding;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Home activity of the application which is displayed when the user opens the app.
 * Displays the list of tasks.
 *
 *
 */
public class MainActivity extends AppCompatActivity implements TasksAdapter.DeleteTaskListener {
    /**
     * List of all projects available in the application
     */
    private List<Project> allProjects;
    /**
     * List of all current tasks of the application
     */
    @NonNull
    private final List<Task> tasks = new ArrayList<>();
    /**
     * The adapter which handles the list of tasks
     */
    private final TasksAdapter adapter = new TasksAdapter(tasks, this);
    /**
     * Dialog to create a new task
     */
    @Nullable
    public AlertDialog dialog = null;
    /**
     * EditText that allows user to set the name of a task
     */
    @Nullable
    private EditText dialogEditText = null;
    /**
     * Spinner that allows the user to associate a project to a task
     */
    @Nullable
    private Spinner dialogSpinner = null;
    private MainViewModel mMainViewModel;
    private DialogAddTaskBinding dialogBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.cleanup.todoc.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        RecyclerView listTasks = binding.listTasks;
        TextView lblNoTasks = binding.lblNoTask;
        listTasks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        listTasks.setAdapter(adapter);
        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        /**
         * getting the list of tasks
         */
        mMainViewModel.getAggregatedTasks().observe(this,new Observer<List<Task>>() {
                    @Override
                    public void onChanged(List<Task> tasks) {
                        Log.d("TAG in Main Activity", "on change: aggregated tasks value is  " + tasks.toString());
                        adapter.updateTasks(tasks);
                        mMainViewModel.updateIsNull();
                    }
                });
        /**
         * Updating the UI if no tasks
         */
        mMainViewModel.getIsNull().observe(this,isNull ->{
            if(isNull){
                lblNoTasks.setVisibility(View.VISIBLE);
                listTasks.setVisibility(View.GONE);
            }else{
                lblNoTasks.setVisibility(View.GONE);
                listTasks.setVisibility(View.VISIBLE);
            }
        });
        /**
         * getting the list of projects for the spinner
         */
        mMainViewModel.getAllProjects().observe(this,projects -> {
            if (projects != null) {
                allProjects = projects;
            }
        });
        binding.fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog();
            }
        });
    }
    /**
     * the method to pass the preference to the View-Model
     * @param preference
     */
    public void setSortingPreference(String preference) {
        mMainViewModel.handleSortingPreference(preference);
    }
    /**
     * Sets the sorting preference
     * @param menu The options menu in which you place your items.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.filter_alphabetical) {
            setSortingPreference("Alphabetical");
        } else if (id == R.id.filter_alphabetical_inverted) {
            setSortingPreference("Alphabetical_Inverted");
        } else if (id == R.id.filter_oldest_first) {
            setSortingPreference("Recent_first");
        } else if (id == R.id.filter_recent_first) {
            setSortingPreference("Old_First");
        } else if (id == R.id.reset_filter) {
            setSortingPreference("All_Tasks");
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Deletes the specified task from the list of tasks
     * @param task The task to be deleted
     * Updates the list of tasks
     */
    @Override
    public void onDeleteTask(Task task) {
        mMainViewModel.delete(task);
    }
    /**
     * Called when the user clicks on the positive button of the Create Task Dialog.
     *
     * @param dialogInterface the current displayed dialog
     */
    private void onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();
            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }
            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError(getString(R.string.empty_task_name));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                Task task = new Task(taskProject,taskName, new Date().getTime());
                mMainViewModel.insert(task);
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }
    /**
     * Shows the Dialog for adding a Task
     */
    private void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();
        dialog.show();
        dialogEditText = dialogBinding.txtTaskName;
        dialogSpinner = dialogBinding.projectSpinner;
        populateDialogSpinner();
    }
    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    private AlertDialog getAddTaskDialog() {
        dialogBinding = DialogAddTaskBinding.inflate(LayoutInflater.from(this));
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.Dialog);
        alertBuilder.setView(dialogBinding.getRoot());
        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog = null;
            }
        });
        dialog = alertBuilder.create();
        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog);
                    }
                });
            }
        });
        return dialog;
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    private void populateDialogSpinner() {
        final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (dialogSpinner != null) {
            dialogSpinner.setAdapter(adapter);
        }
    }

}
