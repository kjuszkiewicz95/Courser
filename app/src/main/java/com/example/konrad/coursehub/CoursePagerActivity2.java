package com.example.konrad.coursehub;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.UUID;

// import android.support.v4.app.Fragment;
// import android.support.v4.app.FragmentManager;
// import android.support.v4.app.FragmentStatePagerAdapter;

public class CoursePagerActivity2 extends FragmentActivity {
    ViewPager mViewPager;
    Course mCourse;
    UUID mSemesterId;
    UUID mCourseId;

    public static final String SELECTED_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String SELECTED_COURSE_ID = "com.example.konrad.coursehub.courseId";
    private static final String TAG = "CoursePagerActivity2: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
            mSemesterId = (UUID)i.getSerializableExtra(SELECTED_SEMESTER_ID);
            mCourseId = (UUID)i.getSerializableExtra(SELECTED_COURSE_ID);
            Semester semester = new Semester();
            for (Semester s: Academics.get(this).getSemesters()) {
                if (s.getId().equals(mSemesterId)) {
                    semester = s;
                    Log.d("DEBUG TAG:", "FOUND SEMESTER");
                }
            }
            for (Course c: semester.getCourses()) {
                if (c.getId().equals(mCourseId)) {
                    mCourse = c;
                    Log.d("DEBUG TAG:", "FOUND COURSE");
                }
            }
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        FragmentManager fm = getFragmentManager();
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                // HERE WILL HAVE NUM OF GRADE CATEGORIES FROM THIS PARTICULAR COURSE
                // WITH POSSIBLY A FEW EXTRA PAGES LIKE "COURSE INFO"
                // WILL PROBABLY HAVE TO PUT AN INTENT ON THIS ACTIVITY WITH COURSES' ID
                return 10000;
            }
            @Override
            public Fragment getItem(int position) {
                return CourseFragment.newInstance(mSemesterId, mCourseId);
            }
        };

        mViewPager.setAdapter(adapter);
        Log.i(TAG, mCourse.getTitle());
        int middlePosition = calculateMiddlePosition(5000, mCourse.getGradeCategories().size());
        mViewPager.setCurrentItem(middlePosition);
    }

    public int calculateMiddlePosition(int defaultMiddle, int size) {
        int actualMiddle = 0;
        int determineOptimalSplit = size / 2;
        int currentPageNum = defaultMiddle % size;
        if (currentPageNum <= determineOptimalSplit) {
            actualMiddle = defaultMiddle - currentPageNum;
        }
        else {
            actualMiddle = defaultMiddle + (size - currentPageNum);
        }
        return actualMiddle;
    }

}
