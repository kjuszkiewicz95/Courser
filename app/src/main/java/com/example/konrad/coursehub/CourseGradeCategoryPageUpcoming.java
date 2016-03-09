package com.example.konrad.coursehub;


import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseGradeCategoryPageUpcoming extends ListFragment {

    Button mPassedButton;
    PassedSelectedListener mCallback;

    UUID mSemesterId;
    UUID mCourseId;
    String mGradeCategoryTitle;
    private Course mCourse;
    private GradeCategory mGradeCategory;

    UpcomingEventsArrayAdapter mAdapter;


    public static final String EXTRA_GRADE_CATEGORY_TITLE = "com.example.konrad.coursehub.gradeCategoryTitle";
    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semeseterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";
    private static final String TAG = "CourseGCPageUpcoming: ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate start: |");
//        if (savedInstanceState == null) {
//            Log.i(TAG,"SAVEDINSTANCE STATE NULL");
//        }
//        if (savedInstanceState != null) {
//            mSemesterId = (UUID)savedInstanceState.getSerializable(EXTRA_SEMESTER_ID);
//            mCourseId = (UUID)savedInstanceState.getSerializable(EXTRA_COURSE_ID);
//            mGradeCategoryTitle = savedInstanceState.getString(EXTRA_GRADE_CATEGORY_TITLE);
//            Log.i(TAG,"WE LOADED TITLE: " + mGradeCategoryTitle);
//        }
//        else {
        if (getArguments() != null) {
            Bundle args = getArguments();
            mSemesterId = (UUID) args.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID) args.getSerializable(EXTRA_COURSE_ID);
            mGradeCategoryTitle = args.getString(EXTRA_GRADE_CATEGORY_TITLE);
        }
        Log.i(TAG, "onCreate middle: | " + mGradeCategoryTitle + " |");
        Semester semester = new Semester();
        for (Semester s: Academics.get(getActivity()).getSemesters()) {
            if (s.getId().equals(mSemesterId)) {
                semester = s;
            }
        }
        for (Course c: semester.getCourses()) {
            if (c.getId().equals(mCourseId)) {
                mCourse = c;
            }
        }
        for (GradeCategory g: mCourse.getGradeCategories()) {
            if(g.getTitle().equals(mGradeCategoryTitle)) {
                mGradeCategory = g;
                Log.i(TAG, "WE FOUND GRADE CATEGORY" + mGradeCategory.getTitle());
            }
        }
        if (mGradeCategory == null) {
            Log.i(TAG, "GRADE CATEGORY NOT FOUND");
        }
    }

    public CourseGradeCategoryPageUpcoming() {
        // Required empty public constructor
    }

    public interface PassedSelectedListener {
        public void onPassedSelected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putSerializable(EXTRA_SEMESTER_ID, mSemesterId);
//        outState.putSerializable(EXTRA_COURSE_ID, mCourseId);
//        outState.putString(EXTRA_GRADE_CATEGORY_TITLE, mGradeCategoryTitle);
//        Log.i(TAG,"WE SAVED TITLE: " + mGradeCategoryTitle);
//        Bundle arguments =  new Bundle();
//        arguments.putSerializable(EXTRA_SEMESTER_ID, mSemesterId);
//        arguments.putSerializable(EXTRA_COURSE_ID, mCourseId);
//        arguments.putString(EXTRA_GRADE_CATEGORY_TITLE, mGradeCategoryTitle);
//        this.setArguments(arguments);
//        Log.i(TAG,"WE SAVED TITLE: " + mGradeCategoryTitle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_course_category_upcoming, container, false);
        TextView pageTitle = (TextView)v.findViewById(R.id.pageTitle);
        pageTitle.setText(mGradeCategoryTitle);
        mPassedButton = (Button)v.findViewById(R.id.passedButton);
        mPassedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CourseGradeCategoryPagePassed passedFragment = new CourseGradeCategoryPagePassed();
//                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragmentUpcomingPassedContainer, passedFragment);
//                transaction.commit();
                  // sendResult(Activity.RESULT_OK);
                mCallback.onPassedSelected();
            }
        });
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        // here I should be setting gradeCategory.getCourseEvents() not course.getCourseEvents()
        //UpcomingEventsArrayAdapter adapter = new UpcomingEventsArrayAdapter(mCourse.getCourseEvents());
        mAdapter = new UpcomingEventsArrayAdapter(mGradeCategory.getCourseEvents());
        listView.setAdapter(mAdapter);
        return v;
    }

    public class UpcomingEventsArrayAdapter extends ArrayAdapter<CourseEvent> {
        public UpcomingEventsArrayAdapter(ArrayList<CourseEvent> courseEvents) {
            super(getActivity(), 0, courseEvents);
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            if  (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_event_small, null);
            }
            Log.i(TAG, "POSITION IS:" + position);
            CourseEvent upcomingEvent = mGradeCategory.getCourseEvents().get(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.list_item_title);
            titleTextView.setText(upcomingEvent.getTitle());
            TextView dateTimeTextView = (TextView) convertView.findViewById(R.id.list_item_dateTime);
            int month = upcomingEvent.getEndDate().get(Calendar.MONTH);
            int dayOfMonth = upcomingEvent.getEndDate().get(Calendar.DAY_OF_MONTH);
            String dateString = month + "/" + dayOfMonth + " | " + upcomingEvent.getStartHour() + ":" + upcomingEvent.getStartMinute();
            dateTimeTextView.setText(dateString);
            TextView daysLeftTextView = (TextView) convertView.findViewById(R.id.list_item_days_left);
//            Calendar cal = Calendar.getInstance();
//            int today = cal.get(Calendar.DAY_OF_MONTH);
//            int daysLeft = upcomingEvent.getDay() - today;
            int daysLeft = courseEventDaysLeft(upcomingEvent.getEndDate());
            daysLeftTextView.setText(Integer.toString(daysLeft));
            return convertView;
        }
    }

    public int courseEventDaysLeft(Calendar eventDate) {
        Calendar current = Calendar.getInstance();
        int daysUntilEvent = 0;
        while (current.before(eventDate)) {
            current.add(Calendar.DAY_OF_MONTH, 1);
            daysUntilEvent++;
        }
        return daysUntilEvent;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (PassedSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PassedSelectedListener");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    public static CourseGradeCategoryPageUpcoming newInstance(UUID semesterId, UUID courseId, String gradeCategoryTitle) {
        CourseGradeCategoryPageUpcoming fragment = new CourseGradeCategoryPageUpcoming();
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
//        i.putExtra(CoursePage.EXTRA_FRAGMENT_TO_STRART, CoursePage.REQUEST_PASSED);
//        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
//    }

}
