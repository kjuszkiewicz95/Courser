package com.example.konrad.coursehub;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseGradeCategoryPagePassed extends ListFragment {
    UpcomingSelectedListener mCallback;
    Button mUpcomingButton;

    UUID mSemesterId;
    UUID mCourseId;
    String mGradeCategoryTitle;
    PassedEventsArrayAdapter mAdapter;
    GradeCategory mGradeCategory;


    public static final String EXTRA_GRADE_CATEGORY_TITLE = "com.example.konrad.coursehub.gradeCategoryTitle";
    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semeseterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mSemesterId = (UUID) args.getSerializable(EXTRA_SEMESTER_ID);
        mCourseId = (UUID) args.getSerializable(EXTRA_COURSE_ID);
        mGradeCategoryTitle = args.getString(EXTRA_GRADE_CATEGORY_TITLE);
        Semester semester = null;
        for (Semester s: Academics.get(getActivity()).getSemesters()) {
            if (s.getId().equals(mSemesterId)) {
                semester = s;
            }
        }
        Course course = null;
        for (Course c: semester.getCourses()) {
            if (c.getId().equals(mCourseId)) {
                course = c;
            }
        }
        for (GradeCategory g: course.getGradeCategories()) {
            if (g.getTitle().equals(mGradeCategoryTitle)) {
                mGradeCategory = g;
            }
        }
    }


    public CourseGradeCategoryPagePassed() {
        // Required empty public constructor
    }

    public interface UpcomingSelectedListener {
        public void onUpcomingSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_course_category_passed, container, false);
        mUpcomingButton = (Button)v.findViewById(R.id.upcomingButton);
        mUpcomingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sendResult(Activity.RESULT_OK);
                mCallback.onUpcomingSelected();
            }
        });
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        mAdapter = new PassedEventsArrayAdapter(mGradeCategory.getPassedEvents());
        listView.setAdapter(mAdapter);
        return v;
    }

    public class PassedEventsArrayAdapter extends ArrayAdapter<CourseEvent> {
        public PassedEventsArrayAdapter(ArrayList<CourseEvent> passedCourseEvents) {
            super(getActivity(), 0, passedCourseEvents);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_event_small_ungraded, null);
            }
            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_title);
            titleTextView.setText(mGradeCategory.getPassedEvents().get(position).getTitle());
            return convertView;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (UpcomingSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement UpcomingSelectedListener");
        }
    }

    public static CourseGradeCategoryPagePassed newInstance(UUID semesterId, UUID courseId, String gradeCategoryTitle) {
        CourseGradeCategoryPagePassed fragment = new CourseGradeCategoryPagePassed();
        Bundle args = new Bundle();
        args.putString(EXTRA_GRADE_CATEGORY_TITLE, gradeCategoryTitle);
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

//    public void sendResult(int resultCode) {
//        if (getTargetFragment() == null) {
//            return;
//        }
//        Intent i = new Intent();
//        i.putExtra(CoursePage.EXTRA_FRAGMENT_TO_STRART, CoursePage.REQUEST_UPCOMING);
//        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
//    }

}
