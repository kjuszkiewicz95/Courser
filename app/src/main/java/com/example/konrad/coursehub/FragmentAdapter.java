package com.example.konrad.coursehub;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Konrad on 11/24/2015.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

    Context mContext;
    Course mCourse;
    UUID mSemesterId;
    UUID mCourseId;
    HashMap<Integer, Fragment> mPageReferenceMap;

    public FragmentAdapter (FragmentManager fm, Context c, UUID semesterId, Course course) {
        super (fm);
        mContext = c;
        mCourse = course;
        mSemesterId = semesterId;
        mCourseId = course.getId();
        mPageReferenceMap = new HashMap<Integer, Fragment>();
    }


    @Override
    public int getIconResId(int index) {
        return 0;
    }

    @Override
    public Fragment getItem (int position) {
        int numGradeCategories = mCourse.getGradeCategories().size();
        int totalPages = numGradeCategories + 3;
        int currentPage = position % totalPages;
        if (currentPage == numGradeCategories) {
            CourseFragment fragment = CourseFragment.newInstance(mSemesterId, mCourseId);
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }
        else if (currentPage < numGradeCategories){
            CoursePage fragment = CoursePage.newInstance(mSemesterId, mCourseId, mCourse.getGradeCategories().get(currentPage).getTitle());
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }
        else {
            // one of the extra pages, but not main
            CoursePage fragment = CoursePage.newInstance(mSemesterId, mCourseId, "Extra");
            mPageReferenceMap.put(position, fragment);
            return fragment;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }


    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }


    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public CharSequence getPageTitle(int position){
        int numGradeCategories = mCourse.getGradeCategories().size();
        int totalPages = numGradeCategories + 3;
        int currentPage = position % totalPages;

        String title = "";
        if (currentPage == numGradeCategories) {
            title = "MAIN";
        }
        else if (currentPage == numGradeCategories + 1) {
            title = "CALENDAR";
        }
        else if (currentPage == numGradeCategories + 2) {
            title = "INFO";
        }
        else { // ONE OF THE GRADE CATEGORY PAGES
            title = mCourse.getGradeCategories().get(currentPage).getTitle();
        }

//        switch(position){
//            case 0:
//                title = "EXAMS";
//                break;
//            case 1:
//                title = "HW";
//                break;
//            case 2:
//                title = "QUIZZES";
//                break;
//            case 3:
//                title = "MAIN";
//                break;
//            case 4:
//                title = "CALENDAR";
//                break;
//            case 5:
//                title = "INFO";
//                break;
//        }
        title = title.toUpperCase();
        return title;
    }
}
