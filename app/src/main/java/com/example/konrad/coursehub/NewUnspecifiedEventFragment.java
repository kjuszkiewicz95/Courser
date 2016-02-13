package com.example.konrad.coursehub;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewUnspecifiedEventFragment extends Fragment {

    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";
    public static final String EXTRA_COURSE_EVENT_ID = "com.example.konrad.coursehub.courseEventId";

    private static final int REQUEST_START_TIME = 0;
    private static final int REQUEST_END_TIME = 1;
    private static final int REQUEST_DATE = 2;
    private static final String DIALOG_START_TIME = "start time";
    private static final String DIALOG_END_TIME = "end time";
    private static final String DIALOG_DATE = "date";

    private final String TAG = "NewUnspecifiedEF: ";

    EditText mEventNameEditText;
    Button mStartTimeButton;
    Button mEndTimeButton;
    Button mDateButton;
    Spinner mGradeCategorySpinner;
    CheckBox mGradedCheckBox;
    ImageView mAddTopicImageView;
    LinearLayout topicsContainer;
    Button mDoneButton;

    RelativeLayout mainLayout;

    UUID semesterId;
    UUID courseId;
    UUID courseEventId;
    Semester mSemester;
    Course mCourse;

    CourseEvent mCourseEvent;

    boolean startTimeChanged = false;
    boolean endTimeChanged = false;
    boolean dateChanged = false;

    private ArrayList<View> eventTopicsViewList;
    int originalCourseEventIndex = 0;

    public NewUnspecifiedEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventTopicsViewList = new ArrayList<View>();

        Bundle args = getArguments();
        semesterId = (UUID) args.getSerializable(EXTRA_SEMESTER_ID);
        courseId = (UUID) args.getSerializable(EXTRA_COURSE_ID);
        courseEventId = (UUID) args.getSerializable(EXTRA_COURSE_EVENT_ID);

        for (Semester s: Academics.get(getActivity()).getSemesters()) {
            if (s.getId().equals(semesterId)) {
                mSemester = s;
            }
        }
        for (Course c: mSemester.getCourses()) {
            if (c.getId().equals(courseId)) {
                mCourse = c;
            }
        }
        if (courseEventId != null) {
            for (int i = 0; i < mCourse.getCourseEvents().size(); i++) {
                UUID eventId = mCourse.getCourseEvents().get(i).getId();
                if (eventId.equals(courseEventId)) {
                    originalCourseEventIndex = i;
                    // MAKING EXACT COPY
                    mCourseEvent = CourseEvent.newInstance(mCourse.getCourseEvents().get(i));
                }
            }
        }
        else {
            // CREATING AN ENTIRELY NEW COURSE EVENT
            mCourseEvent = new CourseEvent();
        }
        // at this point we have course found
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_unspecified_item, null);

        mainLayout = (RelativeLayout)v.findViewById(R.id.mainLayout);

        mEventNameEditText = (EditText)v.findViewById(R.id.event_name_field);
        mEventNameEditText.setTextColor(Color.WHITE);
        mEventNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCourseEvent.setTitle(editable.toString());
            }
        });

        mStartTimeButton = (Button)v.findViewById(R.id.startTimeButton);
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentHour;
                int currentMinute;
                if (startTimeChanged == false) {
                    Calendar cal = Calendar.getInstance();
                    currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    currentMinute = cal.get(Calendar.MINUTE);
                }
                else {
                    currentHour = mCourseEvent.getStartHour();
                    currentMinute = mCourseEvent.getStartMinute();
                }
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(currentHour, currentMinute);
                dialog.setTargetFragment(NewUnspecifiedEventFragment.this, REQUEST_START_TIME);
                dialog.show(fm, DIALOG_START_TIME);
            }
        });

        mEndTimeButton = (Button)v.findViewById(R.id.endTimeButton);
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentHour;
                int currentMinute;
                if (endTimeChanged == false) {
                    Calendar cal = Calendar.getInstance();
                    currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    currentMinute = cal.get(Calendar.MINUTE);
                }
                else {
                    currentHour = mCourseEvent.getEndHour();
                    currentMinute = mCourseEvent.getEndMinute();
                }
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(currentHour, currentMinute);
                dialog.setTargetFragment(NewUnspecifiedEventFragment.this, REQUEST_END_TIME);
                dialog.show(fm, DIALOG_END_TIME);
            }
        });

        mDateButton = (Button)v.findViewById(R.id.dateButton);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentYear;
                int currentMonth;
                int currentDay;

                if (dateChanged == false) {
                    Calendar cal = Calendar.getInstance();
                    currentYear = cal.get(Calendar.YEAR);
                    currentMonth = cal.get(Calendar.MONTH);
                    currentDay = cal.get(Calendar.DAY_OF_MONTH);
                }
                else {
                    currentYear = mCourseEvent.getYear();
                    currentMonth = mCourseEvent.getMonth();
                    currentDay = mCourseEvent.getDay();
                }
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(currentYear, currentMonth, currentDay);
                dialog.setTargetFragment(NewUnspecifiedEventFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });


        // SETTING UP SPINNER
        mGradeCategorySpinner = (Spinner)v.findViewById(R.id.spinner);
        List<String> gradeCategories = new ArrayList<String>();
        for (GradeCategory g: mCourse.getGradeCategories()) {
            gradeCategories.add(g.getTitle());
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_layout, gradeCategories);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGradeCategorySpinner.setAdapter(adapter);
        mGradeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGradeCategory = adapterView.getItemAtPosition(i).toString();
                mCourseEvent.setGradeCategory(selectedGradeCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mGradedCheckBox = (CheckBox)v.findViewById(R.id.gradedCheckBox);
        mGradedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCourseEvent.setGraded(b);
            }
        });


        topicsContainer = (LinearLayout)v.findViewById(R.id.topics_container);
        mAddTopicImageView = (ImageView)v.findViewById(R.id.add_topic_imageView);
        mAddTopicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View newTopicView = inflater.inflate(R.layout.list_item_topic_to_do_edit, null);
                final CourseEventTopic newTopic = new CourseEventTopic();
                mCourseEvent.getTopics().add(newTopic);
                EditText nameEditText = (EditText)newTopicView.findViewById(R.id.nameEditText);
                nameEditText.setTextColor(Color.WHITE);
                nameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        int position = eventTopicsViewList.indexOf(newTopicView);
                        mCourseEvent.getTopics().get(position).setTitle(editable.toString());
                    }
                });
                eventTopicsViewList.add(newTopicView);
                topicsContainer.addView(newTopicView);
                ImageView deleteTopicImageView = (ImageView)newTopicView.findViewById(R.id.delete_imageView);
                deleteTopicImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = eventTopicsViewList.indexOf(newTopicView);
                        mCourseEvent.getTopics().remove(position);
                        eventTopicsViewList.remove(newTopicView);
                        topicsContainer.removeView(newTopicView);
                    }
                });
            }
        });
        mDoneButton = (Button)v.findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EASIER CASE WHERE WE SIMPLY CREATED AN ENTIRELY
                if (courseEventId == null) {
                    mCourse.getCourseEvents().add(mCourseEvent);
                    ArrayList<GradeCategory> courseGradeCategories = mCourse.getGradeCategories();
                    for(GradeCategory g: courseGradeCategories) {
                        if (g.getTitle().equals(mCourseEvent.getGradeCategory())) {
                            g.getCourseEvents().add(mCourseEvent);
                        }
                    }
                    Log.i(TAG, mCourseEvent.getTitle() + "added to" + mCourse.getTitle());
                }
                // WHEN ADDING A NEW COURSE EVENT MAKE SURE TO ADD THE EVENT TO THE PROPER GRADE CATEGORY, THIS HAS NOT BEEN DONE YET
                // do different things depending on whether courseEventId == null or not (whether we are editing existing or adding a new course event)
                else {
                    // REPLACE
                    mCourse.getCourseEvents().set(originalCourseEventIndex, mCourseEvent);
                    // NOW ADD TO SPECIFIC GRADE CATEGORY
                    ArrayList<GradeCategory> courseGradeCategories = mCourse.getGradeCategories();
                    for(GradeCategory g: courseGradeCategories) {
                        if (g.equals(mCourseEvent.getGradeCategory())) {
                            g.getCourseEvents().add(mCourseEvent);
                        }
                    }
                }
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_START_TIME) {
            int startHour = i.getIntExtra(TimePickerFragment.EXTRA_HOUR, 7);
            int startMinute = i.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 7);
            mCourseEvent.setStartHour(startHour);
            mCourseEvent.setStartMinute(startMinute);
            String timeString = timeStringFormatter(startHour, startMinute);
            mStartTimeButton.setText(timeString);
            startTimeChanged = true;
        }
        else if (requestCode == REQUEST_END_TIME) {
            int endHour = i.getIntExtra(TimePickerFragment.EXTRA_HOUR, 7);
            int endMinute = i.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 7);
            mCourseEvent.setEndHour(endHour);
            mCourseEvent.setEndMinute(endMinute);
            String timeString = timeStringFormatter(endHour, endMinute);
            mEndTimeButton.setText(timeString);
            endTimeChanged = true;
        }
        else if (requestCode == REQUEST_DATE) {
            int year = i.getIntExtra(DatePickerFragment.EXTRA_YEAR, 2007);
            int month = i.getIntExtra(DatePickerFragment.EXTRA_MONTH, 7);
            int day = i.getIntExtra(DatePickerFragment.EXTRA_DAY, 7);
            mCourseEvent.setYear(year);
            mCourseEvent.setMonth(month);
            mCourseEvent.setDay(day);
            String dateString = dateStringFormatter(month, day, year);
            mDateButton.setText(dateString);
            dateChanged = true;
        }
    }

    public String dateStringFormatter(int month, int day, int year) {
        month++;
        String monthString = Integer.toString(month);
        String dayString = Integer.toString(day);
        if (month < 10) {
            monthString = 0 + monthString;
        }
        if (day < 10) {
            dayString = 0 + dayString;
        }
        String returnString = monthString + "/" + dayString + "/" + year;
        return returnString;

    }

    public String timeStringFormatter(int hour, int minute) {
        String hourString = Integer.toString(hour);
        String minuteString = Integer.toString(minute);
        if(hour < 10) {
            hourString = "0" + Integer.toString(hour);
        }
        if(minute < 10) {
            minuteString = "0" + Integer.toString(minute);
        }
        String returnString = hourString + ":" + minuteString;
        return returnString;
    }

    public static NewUnspecifiedEventFragment newInstance (UUID semesterId, UUID courseId) {
        NewUnspecifiedEventFragment fragment = new NewUnspecifiedEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewUnspecifiedEventFragment newEditInstance(UUID semesterId, UUID courseId, UUID courseEventId) {
        NewUnspecifiedEventFragment fragment = new NewUnspecifiedEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        args.putSerializable(EXTRA_COURSE_EVENT_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }
}
