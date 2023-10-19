package com.cleanup.todoc;



import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.cleanup.todoc.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cleanup.todoc.RecyclerViewItemCountAssertion.withItemCount;
import static com.cleanup.todoc.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.contrib.RecyclerViewActions;


/**
 * Instrumented test, which will execute on an Android device.
 *
 *
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {
    private static final int ITEM_COUNT = 1;
    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addAndRemoveTask() {

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("Tâche example"));
        onView(withId(android.R.id.button1)).perform(click());
        // Check that lblTask is not displayed anymore
        onView(withId(R.id.lbl_no_task)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        // Check that recyclerView is displayed
        onView(withId(R.id.list_tasks)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        //assertThat(listTasks.getVisibility(), equalTo(View.VISIBLE));
        // Check that it contains two elements
        onView(withId(R.id.list_tasks)).check(withItemCount(ITEM_COUNT+1));


        onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteTaskViewAction()));
        // Wait briefly to ensure item removal
        try {
            Thread.sleep(1000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.list_tasks)).check(withItemCount(ITEM_COUNT));
        onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteTaskViewAction()));
        // Wait briefly to ensure item removal
        try {
            Thread.sleep(1000); // Adjust the sleep duration as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.list_tasks)).check(withItemCount(ITEM_COUNT-1));
        // Check that lblTask is displayed
        onView(withId(R.id.lbl_no_task)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        //assertThat(lblNoTask.getVisibility(), equalTo(View.VISIBLE));
        // Check that recyclerView is not displayed anymore
        onView(withId(R.id.list_tasks)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
        //assertThat(listTasks.getVisibility(), equalTo(View.GONE));
    }

    @Test
    public void sortTasks() {

        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("aaa Tâche example"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Lucidia")).inRoot(isPlatformPopup()).perform(click());
        //onData(is("Projet Lucidia")).inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("zzz Tâche example"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Circus")).inRoot(isPlatformPopup()).perform(click());
        //onData(is("Projet Circus")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText("hhh Tâche example"));
        onView(withId(R.id.project_spinner)).perform(click());
        onView(withText("Projet Tartampion")).inRoot(isPlatformPopup()).perform(click());
        //onData(is("Projet Tartampion")).perform(click());
        onView(withId(android.R.id.button1)).perform(click());


        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("new Task")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(3, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("new Task")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(3, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort alphabetical inverted
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_alphabetical_invert)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("new Task")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(3, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));

        // Sort old first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_oldest_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("new Task")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(3, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));

        // Sort recent first
        onView(withId(R.id.action_filter)).perform(click());
        onView(withText(R.string.sort_recent_first)).perform(click());
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(0, R.id.lbl_task_name))
                .check(matches(withText("hhh Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(1, R.id.lbl_task_name))
                .check(matches(withText("zzz Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(2, R.id.lbl_task_name))
                .check(matches(withText("aaa Tâche example")));
        onView(withRecyclerView(R.id.list_tasks).atPositionOnView(3, R.id.lbl_task_name))
                .check(matches(withText("new Task")));
    }
}
