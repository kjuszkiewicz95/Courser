package com.example.konrad.coursehub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Konrad on 8/14/2015.
 */
public class Course {

    private static final String JSON_TITLE = "title";
    private static final String JSON_START_HOUR = "startHour";
    private static final String JSON_START_MINUTE = "startMinute";
    private static final String JSON_END_HOUR = "endHour";
    private static final String JSON_END_MINUTE = "endMinute";
    private static final String JSON_DAYS = "days";
    private static final String JSON_GRADE_CATEGORIES = "gradeCategories";
    private static final String JSON_ID = "id";

    private String mTitle;
    private int mStartHour;
    private int mStartMinute;
    private int mEndHour;
    private int mEndMinute;
    private ArrayList<String> mDays;
    private ArrayList<GradeCategory> mGradeCategories;
    private ArrayList<CourseEvent> mCourseEvents;
    private boolean mStartTimeSet;
    private boolean mEndTimeSet;
    private UUID mId;

    public Course() {
        mDays = new ArrayList<String>();
        mGradeCategories = new ArrayList<GradeCategory>();
        mCourseEvents = new ArrayList<CourseEvent>();
        mId= UUID.randomUUID();
    }

    public Course(JSONObject json) throws JSONException{
        mTitle = json.getString(JSON_TITLE);
        mStartHour = json.getInt(JSON_START_HOUR);
        mStartMinute = json.getInt(JSON_START_MINUTE);
        mEndHour = json.getInt(JSON_END_HOUR);
        mEndMinute = json.getInt(JSON_END_MINUTE);
        mDays = new ArrayList<String>();
        JSONArray daysJSONArray = json.getJSONArray(JSON_DAYS);
        for (int i = 0; i < daysJSONArray.length(); i++) {
            mDays.add((String)daysJSONArray.get(i));
        }
        mGradeCategories = new ArrayList<GradeCategory>();
        JSONArray gradeCategoriesJSONArray = json.getJSONArray(JSON_GRADE_CATEGORIES);
        for (int i = 0; i < gradeCategoriesJSONArray.length(); i++) {
            mGradeCategories.add(new GradeCategory(gradeCategoriesJSONArray.getJSONObject(i)));
        }
        if (json.getString(JSON_ID) != null) {
            mId = UUID.fromString(json.getString(JSON_ID));
        }
        mId = UUID.fromString(json.getString(JSON_ID));
    }


    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_START_HOUR, mStartHour);
        json.put(JSON_START_MINUTE, mStartMinute);
        json.put(JSON_END_HOUR, mEndHour);
        json.put(JSON_END_MINUTE, mEndMinute);
        JSONArray daysJSONArray = new JSONArray(mDays);
        json.put(JSON_DAYS, daysJSONArray);

        JSONArray gradeCategoriesJSONArray = new JSONArray();
        for (GradeCategory g: mGradeCategories) {
            gradeCategoriesJSONArray.put(g.toJSON());
        }
        json.put(JSON_GRADE_CATEGORIES, gradeCategoriesJSONArray);
        json.put(JSON_ID, mId);
        return json;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getStartHour() {
        return mStartHour;
    }

    public void setStartHour(int startHour) {
        mStartHour = startHour;
    }

    public int getStartMinute() {
        return mStartMinute;
    }

    public void setStartMinute(int startMinute) {
        mStartMinute = startMinute;
    }

    public int getEndHour() {
        return mEndHour;
    }

    public void setEndHour(int endHour) {
        mEndHour = endHour;
    }

    public int getEndMinute() {
        return mEndMinute;
    }

    public void setEndMinute(int endMinute) {
        mEndMinute = endMinute;
    }

    public ArrayList<String> getDays() {
        return mDays;
    }

    public void setDays(ArrayList<String> days) {
        mDays = days;
    }

    public boolean isStartTimeSet() {
        return mStartTimeSet;
    }

    public void setStartTimeSet(boolean startTimeSet) {
        mStartTimeSet = startTimeSet;
    }


    public boolean isEndTimeSet() {
        return mEndTimeSet;
    }

    public void setEndTimeSet(boolean endTimeSet) {
        mEndTimeSet = endTimeSet;
    }

    public ArrayList<GradeCategory> getGradeCategories() {
        return mGradeCategories;
    }

    public void setGradeCategories(ArrayList<GradeCategory> gradeCategories) {
        mGradeCategories = gradeCategories;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public ArrayList<CourseEvent> getCourseEvents() {
        return mCourseEvents;
    }

    public void setCourseEvents(ArrayList<CourseEvent> courseEvents) {
        mCourseEvents = courseEvents;
    }
}
