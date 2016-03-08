package com.example.konrad.coursehub;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class CoursePage extends Fragment {

    public static final String EXTRA_GRADE_CATEGORY_TITLE = "com.example.konrad.coursehub.gradeCategoryTitle";
    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semeseterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";
    private static final String TAG = "CourseGCPage: ";

    String mGradeCategoryTitle;
    UUID mSemesterId;
    UUID mCourseId;

    public CoursePage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.i(TAG, "GRADE CATEGORY PAGE CREATED NOT NEW INSTANCE");
            mSemesterId = (UUID) savedInstanceState.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID) savedInstanceState.getSerializable(EXTRA_COURSE_ID);
            mGradeCategoryTitle = savedInstanceState.getString(EXTRA_GRADE_CATEGORY_TITLE);
        }
        else {
            Log.i(TAG, "GRADE CATEGORY PAGE CREATED NEW INSTANCE");
            Bundle args = getArguments();
            mSemesterId = (UUID) args.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID) args.getSerializable(EXTRA_COURSE_ID);
            mGradeCategoryTitle = args.getString(EXTRA_GRADE_CATEGORY_TITLE);
        }
        if (!mGradeCategoryTitle.equals("Extra")) {
            CourseGradeCategoryPageUpcoming upcomingFragment = CourseGradeCategoryPageUpcoming.newInstance(mSemesterId, mCourseId, mGradeCategoryTitle);
            // upcomingFragment.setTargetFragment(CoursePage.this, REQUEST_UPCOMING);
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentPageContainer, upcomingFragment).commit();
        }
        else {
            CourseExtraPage extraFragment = new CourseExtraPage();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentPageContainer, extraFragment).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course_grade_category_page, container, false);

        return v;
    }

    public void switchToPassed() {
        CourseGradeCategoryPagePassed passedFragment = CourseGradeCategoryPagePassed.newInstance(mSemesterId, mCourseId, mGradeCategoryTitle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentPageContainer, passedFragment).commit();
    }

    public void switchToUpcoming() {
        CourseGradeCategoryPageUpcoming upcomingFragment = CourseGradeCategoryPageUpcoming.newInstance(mSemesterId, mCourseId, mGradeCategoryTitle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentPageContainer, upcomingFragment).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_SEMESTER_ID, mSemesterId);
        outState.putSerializable(EXTRA_COURSE_ID, mCourseId);
        outState.putString(EXTRA_GRADE_CATEGORY_TITLE, mGradeCategoryTitle);
    }
    public static CoursePage newInstance(UUID semesterId, UUID courseId, String gradeCategoryTitle) {
        Log.i(TAG, "GRADE CATEGORY PAGE CREATED IS A NEW INSTANCE");
        CoursePage fragment = new CoursePage();
        Bundle args = new Bundle();
        args.putString(EXTRA_GRADE_CATEGORY_TITLE, gradeCategoryTitle);
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent i) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }
//        int fragmentToStartCode = i.getIntExtra(EXTRA_FRAGMENT_TO_STRART, 0);
//        if (fragmentToStartCode == REQUEST_UPCOMING) {
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            CourseGradeCategoryPageUpcoming upcomingFragment = new CourseGradeCategoryPageUpcoming();
//            upcomingFragment.setTargetFragment(CoursePage.this, REQUEST_UPCOMING);
//            transaction.replace(R.id.fragmentUpcomingPassedContainer, upcomingFragment).commit();
//
//        }
//        else if (fragmentToStartCode == REQUEST_PASSED) {
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            CourseGradeCategoryPagePassed passedFragment = new CourseGradeCategoryPagePassed();
//            passedFragment.setTargetFragment(CoursePage.this, REQUEST_PASSED);
//            transaction.replace(R.id.fragmentUpcomingPassedContainer, passedFragment).commit();
//        }
//        else {
//            // do nothing for right now
//        }
//
//    }








}
