package com.example.konrad.coursehub;

import android.content.Context;

import java.util.Calendar;

/**
 * Created by Konrad on 3/9/2016.
 */
public class CourseUpdater {
    private static CourseUpdater sCourseUpdater;
    private Context mAppContext;

    private CourseUpdater(Context appContext) {
        mAppContext = appContext;
    }

    public static CourseUpdater get(Context c) {
        if (sCourseUpdater == null) {
            sCourseUpdater = new CourseUpdater(c.getApplicationContext());
        }
        return sCourseUpdater;
    }

    public void updatePastEvents(Course courseToSearch) {
        Calendar currentDate = Calendar.getInstance();
        for (GradeCategory g: courseToSearch.getGradeCategories()) {
            for (CourseEvent c: g.getUpcomingEvents()) {
                if (!currentDate.before(c.getEndDate())) {
                    markEventPassed(g, c);
                }
            }
        }
    }

    public void markEventPassed(GradeCategory gradeCategory, CourseEvent passedCourseEvent) {
        gradeCategory.getUpcomingEvents().remove(passedCourseEvent);
        gradeCategory.getPassedEvents().add(passedCourseEvent);
    }
}
