package com.cleanup.todoc.utils;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.cleanup.todoc.R;

import org.hamcrest.Matcher;

public class DeleteTaskViewAction implements ViewAction {
    @Override
    public Matcher<View> getConstraints() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Click on specific button";
    }

    @Override
    public void perform(UiController uiController, View view) {
        View deleteButton = view.findViewById(R.id.img_delete);
        deleteButton.performClick();

    }
}
