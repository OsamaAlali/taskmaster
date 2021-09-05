package com.example.taskmaster;


import android.app.Instrumentation;
import android.view.View;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import junit.extensions.ActiveTestSuite;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest  {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void addTask() {
        onView(withId(R.id.addTaskBtn)).perform(ViewActions.click());
    }

    @Test
    public void textView() {
        onView(withId(R.id.textView4)).check(matches(withText("My Task")));

    }

    @Test
    public void settingPage() {
       onView(withId(R.id.settingsId)).perform(ViewActions.click());
        String username="osama";
        onView(withId(R.id.userNameId)).perform(typeText(username));
        onView(withId(R.id.saveId)).perform(ViewActions.click()) ;
        onView(withId(R.id.userNameHomeId)).check(matches(withText(username+"'s tasks")));

    }




}
