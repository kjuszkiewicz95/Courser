package com.example.konrad.coursehub;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Konrad on 8/14/2015.
 */
public class Semester {

    private static final String JSON_SEASON = "season";
    private static final String JSON_YEAR = "year";
    private static final String JSON_ID = "id";
    private static final String JSON_COURSES = "courses";

    private String mSeason;
    private int mYear;
    private ArrayList<Course> courses;
    private UUID mId;

    public Semester() {
        courses = new ArrayList<Course>();
        mId = UUID.randomUUID();
    }

    public Semester(JSONObject json) throws JSONException {
        mSeason = json.getString(JSON_SEASON);
        mYear = json.getInt(JSON_YEAR);
        courses = new ArrayList<Course>();
        JSONArray coursesJSONArray = json.getJSONArray(JSON_COURSES);
        for (int i = 0; i < coursesJSONArray.length(); i++) {
            courses.add(new Course(coursesJSONArray.getJSONObject(i)));
        }
        mId = UUID.fromString(json.getString(JSON_ID));
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_SEASON, mSeason);
        json.put(JSON_YEAR, mYear);
        json.put(JSON_ID, mId);

        JSONArray coursesJSONArray = new JSONArray();
        for (Course c: courses) {
            coursesJSONArray.put(c.toJSON());
        }
        json.put(JSON_COURSES, coursesJSONArray);
        return json;
    }


    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getSeason() {
        return mSeason;
    }

    public void setSeason(String season) {
        mSeason = season;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }
}
