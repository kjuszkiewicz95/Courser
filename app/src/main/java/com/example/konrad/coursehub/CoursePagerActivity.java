package com.example.konrad.coursehub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.UUID;

/**
 * Created by Konrad on 11/24/2015.
 */
public class CoursePagerActivity extends FragmentActivity implements CourseGradeCategoryPageUpcoming.PassedSelectedListener, CourseGradeCategoryPagePassed.UpcomingSelectedListener {

    public static final String SELECTED_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String SELECTED_COURSE_ID = "com.example.konrad.coursehub.courseId";

    FragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;

    Course mCourse;
    UUID mSemesterId;
    UUID mCourseId;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_pager);

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

        mAdapter = new FragmentAdapter(getFragmentManager(), this, mSemesterId, mCourse);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        int middlePosition = calculateMiddlePosition(25, mCourse.getGradeCategories().size() + 3);
        // EXTRA 3 PAGES FOR MAIN, CALENDAR, INFO
        int mainPagePosition = middlePosition + mCourse.getGradeCategories().size();

        mPager.setCurrentItem(mainPagePosition);
        mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

    public void onPassedSelected() {
        int index = mPager.getCurrentItem();
        CourseGradeCategoryPage fragment = (CourseGradeCategoryPage) mAdapter.getFragment(index);
        fragment.switchToPassed();
    }

    public void onUpcomingSelected() {
        int index = mPager.getCurrentItem();
        CourseGradeCategoryPage fragment = (CourseGradeCategoryPage) mAdapter.getFragment(index);
        fragment.switchToUpcoming();
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
