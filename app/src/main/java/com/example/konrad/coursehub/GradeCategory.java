package com.example.konrad.coursehub;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Konrad on 8/23/2015.
 */
public class GradeCategory {

    private static final String JSON_TITLE = "title";
    private static final String JSON_PERCENTAGE = "percentage";
    private static final String JSON_PERCENTAGE_SET = "percentageSet";

    private String mTitle;
    private double mPercentage;
    private boolean mPercentageSet = false;
    private ArrayList<CourseEvent> mCourseEvents;
    private ArrayList<CourseEvent> mUpcomingEvents;
    private ArrayList<CourseEvent> mPassedEvents;


    public GradeCategory() {
        mCourseEvents = new ArrayList<CourseEvent>();
        mUpcomingEvents = new ArrayList<CourseEvent>();
        mPassedEvents = new ArrayList<CourseEvent>();
    }

    public GradeCategory(JSONObject json) throws JSONException {
        mTitle = json.getString(JSON_TITLE);
        mPercentage = json.getDouble(JSON_PERCENTAGE);
        mPercentageSet = json.getBoolean(JSON_PERCENTAGE_SET);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_PERCENTAGE, mPercentage);
        json.put(JSON_PERCENTAGE_SET, mPercentageSet);
        return json;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public double getPercentage() {
        return mPercentage;
    }

    public void setPercentage(double percentage) {
        mPercentage = percentage;
    }

    public boolean isPercentageSet() {
        return mPercentageSet;
    }

    public void setPercentageSet(boolean percentageSet) {
        mPercentageSet = percentageSet;
    }

    public ArrayList<CourseEvent> getCourseEvents() {
        return mCourseEvents;
    }

    public void setCourseEvents(ArrayList<CourseEvent> courseEvents) {
        mCourseEvents = courseEvents;
    }

    public ArrayList<CourseEvent> getPassedEvents() {
        return mPassedEvents;
    }

    public void setPassedEvents(ArrayList<CourseEvent> passedEvents) {
        mPassedEvents = passedEvents;
    }

    public ArrayList<CourseEvent> getUpcomingEvents() {
        return mUpcomingEvents;
    }

    public void setUpcomingEvents(ArrayList<CourseEvent> upcomingEvents) {
        mUpcomingEvents = upcomingEvents;
    }
}
