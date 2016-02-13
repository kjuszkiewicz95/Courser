package com.example.konrad.coursehub;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Konrad on 12/21/2015.
 */
public class CourseEvent {
    private ArrayList<CourseEventTopic> mTopics;
    private String mGradeCategory;
    private String mTitle;
    private boolean mGraded = false;
    private int mStartHour;
    private int mStartMinute;
    private int mEndHour;
    private int mEndMinute;
    private int mMonth;
    private int mDay;
    private int mYear;
    private UUID mId;

    public CourseEvent() {
        mTopics = new ArrayList<CourseEventTopic>();
        mId = UUID.randomUUID();
    }

    public static CourseEvent newInstance(CourseEvent courseEventToDuplicate) {
        CourseEvent duplicateCourseEvent = new CourseEvent();
        duplicateCourseEvent.setTopics(courseEventToDuplicate.getTopics());
        duplicateCourseEvent.setGradeCategory(courseEventToDuplicate.getGradeCategory());
        duplicateCourseEvent.setTitle(courseEventToDuplicate.getTitle());
        duplicateCourseEvent.setGraded(courseEventToDuplicate.isGraded());
        duplicateCourseEvent.setStartHour(courseEventToDuplicate.getStartHour());
        duplicateCourseEvent.setStartMinute(courseEventToDuplicate.getStartMinute());
        duplicateCourseEvent.setEndHour(courseEventToDuplicate.getEndHour());
        duplicateCourseEvent.setEndMinute(courseEventToDuplicate.getEndMinute());
        duplicateCourseEvent.setMonth(courseEventToDuplicate.getMonth());
        duplicateCourseEvent.setDay(courseEventToDuplicate.getDay());
        duplicateCourseEvent.setYear(courseEventToDuplicate.getYear());
        // do not copy UUID
        return duplicateCourseEvent;
    }


    public ArrayList<CourseEventTopic> getTopics() {
        return mTopics;
    }

    public void setTopics(ArrayList<CourseEventTopic> topics) {
        mTopics = topics;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isGraded() {
        return mGraded;
    }

    public void setGraded(boolean graded) {
        this.mGraded = graded;
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

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getGradeCategory() {
        return mGradeCategory;
    }

    public void setGradeCategory(String gradeCategory) {
        mGradeCategory = gradeCategory;
    }


    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }
}
