package com.example.konrad.coursehub;

import android.app.Fragment;
import android.content.Intent;

import java.util.UUID;

/**
 * Created by Konrad on 12/20/2015.
 */
public class NewUnspecifiedEventActivity extends SingleFragmentActivity {

    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";

    public Fragment createFragment() {
        Intent i = getIntent();
        UUID mSemesterId = (UUID)i.getSerializableExtra(EXTRA_SEMESTER_ID);
        UUID mCourseId = (UUID)i.getSerializableExtra(EXTRA_COURSE_ID);

        return NewUnspecifiedEventFragment.newInstance(mSemesterId, mCourseId);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }
}
