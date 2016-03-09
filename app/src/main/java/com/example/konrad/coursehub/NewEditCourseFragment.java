package com.example.konrad.coursehub;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewEditCourseFragment extends Fragment {

    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String EXTRA_COURSE_ID = "com.exmpample.konrad.coursehub.courseId";
    private static final String TAG = "NewEditCourseFragment: ";
    Course mCourse;
    Semester mSemester;
    TextView titleEditText;
    Button mStartTimeButton;
    Button mEndTimeButton;
    CheckBox monCB;
    CheckBox tueCB;
    CheckBox wedCB;
    CheckBox thurCB;
    CheckBox friCB;
    CheckBox satCB;
    CheckBox sunCB;
    ImageView mAddItem;
    Button mDoneButton;
    private HashMap<Integer, String> categoryEditTextList;
    private HashMap<Integer, Double> percentageEditTextList;
    LinearLayout courseBreakdownsContainer;
    private ArrayList<View> courseBreakdownsViewList;

    private static final int REQUEST_START_TIME = 0;
    private static final int REQUEST_END_TIME = 1;
    private static final String DIALOG_START_TIME = "start time";
    private static final String DIALOG_END_TIME = "end time";

    UUID mSemesterId;
    UUID mCourseId;

    int mOriginalCourseEventIndex = 0;

    boolean startTimeChanged = false;
    boolean endTimeChanged = false;

    public NewEditCourseFragment() {
        // Required empty public constructor
        // then eventually we have to handle case of simply editing course

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        categoryEditTextList = new HashMap<Integer, String>();
        percentageEditTextList = new HashMap<Integer, Double>();
        courseBreakdownsViewList = new ArrayList<View>();

        // if we are adding a new course to an existing semester
        if (arguments != null && (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID) != null && (UUID)arguments.getSerializable(EXTRA_COURSE_ID) != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID)arguments.getSerializable(EXTRA_COURSE_ID);
            mSemester = new Semester();
            for (Semester s: Academics.get(getActivity()).getSemesters()) {
                if (s.getId() == mSemesterId) {
                    mSemester = s;
                }
            }
            for (int i = 0; i < mSemester.getCourses().size(); i++) {
                Course c = mSemester.getCourses().get(i);
                if (c.getId() == mCourseId) {
                    mOriginalCourseEventIndex = i;
                    // MAKING EXACT COPY
                    mCourse = Course.newInstance(c);
                }
            }
        }
        else if (arguments!= null && (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID) != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mCourse = new Course();
            Log.i(TAG, "New course has been made.");
        }
        else {
            // do nothing, have to be either creating a new course or editing a current course
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_edit_course, null);
        titleEditText = (EditText)v.findViewById(R.id.course_name_field);
        titleEditText.setTextColor(Color.WHITE);
        titleEditText.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });
        if (mCourseId != null) {
            titleEditText.setText(mCourse.getTitle());
        }
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCourse.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mStartTimeButton = (Button)v.findViewById(R.id.startTimeButton);
        if (mCourseId != null) {
            int startHour = mCourse.getStartHour();
            int startMinute = mCourse.getStartMinute();
            String timeString = timeStringFormatter(startHour, startMinute);
            mStartTimeButton.setText(timeString);
        }
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour;
                int currentMin;
                if (startTimeChanged || mCourseId != null) {
                    currentHour = mCourse.getStartHour();
                    currentMin = mCourse.getStartMinute();
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    currentMin = cal.get(Calendar.MINUTE);
                }
                TimePickerFragment dialog = TimePickerFragment.newInstance(currentHour, currentMin);
                dialog.setTargetFragment(NewEditCourseFragment.this, REQUEST_START_TIME);
                FragmentManager fm = getFragmentManager();
                dialog.show(fm, DIALOG_START_TIME);
            }
        });
        mEndTimeButton = (Button)v.findViewById(R.id.endTimeButton);
        if (mCourseId != null) {
            int endHour = mCourse.getEndHour();
            int endMinute = mCourse.getEndMinute();
            String timeString = timeStringFormatter(endHour, endMinute);
            mEndTimeButton.setText(timeString);
        }
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour;
                int currentMin;
                if (endTimeChanged || mCourseId != null) {
                    currentHour = mCourse.getEndHour();
                    currentMin = mCourse.getEndMinute();
                }
                else {
                    Calendar cal = Calendar.getInstance();
                    currentHour = cal.get(Calendar.HOUR_OF_DAY);
                    currentMin = cal.get(Calendar.MINUTE);
                }
                TimePickerFragment dialog = TimePickerFragment.newInstance(currentHour, currentMin);
                dialog.setTargetFragment(NewEditCourseFragment.this, REQUEST_END_TIME);
                FragmentManager fm = getFragmentManager();
                dialog.show(fm, DIALOG_END_TIME);
            }
        });
        monCB = (CheckBox)v.findViewById(R.id.mon_checkBox);
        tueCB = (CheckBox)v.findViewById(R.id.tue_checkBox);
        wedCB = (CheckBox)v.findViewById(R.id.wed_checkBox);
        thurCB = (CheckBox)v.findViewById(R.id.thur_checkBox);
        friCB = (CheckBox)v.findViewById(R.id.fri_checkBox);
        satCB = (CheckBox)v.findViewById(R.id.sat_checkBox);
        sunCB = (CheckBox)v.findViewById(R.id.sun_checkBox);
        if (mCourseId != null) {
            for (int i = 0; i < mCourse.getDays().size(); i++) {
                switch (mCourse.getDays().get(i)) {
                    case "Monday":
                        monCB.setChecked(true);
                        break;
                    case "Tuesday":
                        tueCB.setChecked(true);
                        break;
                    case "Wednesday":
                        wedCB.setChecked(true);
                        break;
                    case "Thursday":
                        thurCB.setChecked(true);
                        break;
                    case "Friday":
                        friCB.setChecked(true);
                        break;
                    case "Saturday":
                        satCB.setChecked(true);
                        break;
                    case "Sunday":
                        sunCB.setChecked(true);
                        break;
                    default:
                        break;
                }
            }
        }
        monCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mCourse.getDays().add("Monday");
                } else {
                    mCourse.getDays().remove("Monday");
                }
            }
        });
        tueCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Tuesday");
                }
                else {
                    mCourse.getDays().remove("Tuesday");
                }
            }
        });
        wedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Wednesday");
                }
                else {
                    mCourse.getDays().remove("Wednesday");
                }
            }
        });
        thurCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Thursday");
                }
                else {
                    mCourse.getDays().remove("Thursday");
                }
            }
        });
        friCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Friday");
                }
                else {
                    mCourse.getDays().remove("Friday");
                }
            }
        });
        satCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Saturday");
                }
                else {
                    mCourse.getDays().remove("Saturday");
                }
            }
        });
        sunCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCourse.getDays().add("Sunday");
                }
                else {
                    mCourse.getDays().remove("Sunday");
                }
            }
        });

        //Linear layout which will hold the children linearlayouts (list_item_grade_breadowns);
        courseBreakdownsContainer = (LinearLayout)v.findViewById(R.id.course_breakdowns_container);
        mAddItem = (ImageView)v.findViewById(R.id.add_topic_imageView);

        // in case of editing course we have to add all existing gradeCategories to list and container view
        if (mCourseId != null && !mCourse.getGradeCategories().isEmpty()) {
            // have to add all of the gradecategory views dynamically
            for (GradeCategory g: mCourse.getGradeCategories()) {
                final LinearLayout courseBreakdownView = (LinearLayout)inflater.inflate(R.layout.list_item_grade_breakdown, null);

                EditText categoryNameEditText = (EditText)courseBreakdownView.findViewById(R.id.categoryName);
                if (g.getTitle() != null) {
                    categoryNameEditText.setText(g.getTitle());
                }
                categoryNameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        mCourse.getGradeCategories().get(position).setTitle(editable.toString());
                    }
                });
                EditText percentEditText = (EditText)courseBreakdownView.findViewById(R.id.percent_editText);
                percentEditText.setText(Double.toString(g.getPercentage()));
                percentEditText.setSelectAllOnFocus(true);
                percentEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        mCourse.getGradeCategories().get(position).setPercentage(Double.parseDouble(editable.toString()));
                    }
                });
                ImageView deleteButton = (ImageView)courseBreakdownView.findViewById(R.id.delete_imageView);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        // remove this gradecategory from the course object
                        mCourse.getGradeCategories().remove(position);
                        // remove this gradecategory view from the grade courseBreakdownsViewList
                        courseBreakdownsViewList.remove(position);
                        // finally remove this view entirely from our container
                        courseBreakdownsContainer.removeView(courseBreakdownView);
                    }
                });
                // adding courseBreakDownView to list and container view
                courseBreakdownsViewList.add(courseBreakdownView);
                courseBreakdownsContainer.addView(courseBreakdownView);
            }
        }
        // handles the adding of new gradeCategories
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradeCategory category = new GradeCategory();
                final LinearLayout courseBreakdownView = (LinearLayout)inflater.inflate(R.layout.list_item_grade_breakdown, null);

                EditText categoryNameEditText = (EditText)courseBreakdownView.findViewById(R.id.categoryName);
                categoryNameEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        mCourse.getGradeCategories().get(position).setTitle(editable.toString());
                    }
                });
                EditText percentEditText = (EditText)courseBreakdownView.findViewById(R.id.percent_editText);
                // SET ESTIMATED PERCENT
                double estimatedPercent = calculateEstimatedPercentage();
                percentEditText.setText(Double.toString(estimatedPercent));
                percentEditText.setSelectAllOnFocus(true);
                category.setPercentage(estimatedPercent);
                percentEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        if (editable.length() >0 ) {
                            mCourse.getGradeCategories().get(position).setPercentage(Double.parseDouble(editable.toString()));
                        }
                    }
                });
                ImageView deleteButton = (ImageView)courseBreakdownView.findViewById(R.id.delete_imageView);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        // removing GradeCategory from Course object, and removing GradeCategory view from list and container view
                        mCourse.getGradeCategories().remove(position);
                        courseBreakdownsContainer.removeView(courseBreakdownView);
                        courseBreakdownsViewList.remove(courseBreakdownView);
                    }
                });
                // adding GradeCategory to Course object, and adding GradeCategory view to list and container view
                mCourse.getGradeCategories().add(category);
                courseBreakdownsContainer.addView(courseBreakdownView);
                courseBreakdownsViewList.add(courseBreakdownView);
            } // end of add grade breakdown onclick method
        });
        mDoneButton = (Button)v.findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add course to semester if we were creating new course and navigate to course main page
                if (mCourseId == null) {
                    for (Semester s : Academics.get(getActivity()).getSemesters()) {
                        if (s.getId() == mSemesterId) {
                            s.getCourses().add(mCourse);
                        }
                    }
                    // from fragment 2 I go back to fragment 1 and then straight to fragment 3. This way the back button will bring me again from 3 to 1
                    Academics.get(getActivity()).saveSemesters();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                    Intent i = new Intent(getActivity(), CoursePagerActivity.class);
                    i.putExtra(CoursePagerActivity.SELECTED_SEMESTER_ID, mSemesterId);
                    i.putExtra(CoursePagerActivity.SELECTED_COURSE_ID, mCourse.getId());
                    startActivity(i);
                }
                // if we were editing a course, simply navigate back to courses list of that semester
                else {
                    // REPLACE
                    mSemester.getCourses().set(mOriginalCourseEventIndex, mCourse);
                    Academics.get(getActivity()).saveSemesters();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                }
            }
        });
        return v;
    }

    public double calculateEstimatedPercentage() {
        int totalPercentage = 0;
        for (GradeCategory g: mCourse.getGradeCategories()) {
            totalPercentage += g.getPercentage();
        }
        if (totalPercentage < 100) {
            return 100 - totalPercentage;
        }
        else {
            return 0;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        int hour = i.getIntExtra(TimePickerFragment.EXTRA_HOUR, 7);
        int minute = i.getIntExtra(TimePickerFragment.EXTRA_MINUTE, 7);
        if (requestCode == REQUEST_START_TIME) {
            mCourse.setStartHour(hour);
            mCourse.setStartMinute(minute);
            mStartTimeButton.setText(timeStringFormatter(hour, minute));
            startTimeChanged = true;
        }
        if (requestCode == REQUEST_END_TIME) {
            mCourse.setEndHour(hour);
            mCourse.setEndMinute(minute);
            mEndTimeButton.setText(timeStringFormatter(hour, minute));
            endTimeChanged = true;
        }
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

    public static NewEditCourseFragment newCourseInstance(UUID semesterId) {
        NewEditCourseFragment fragment = new NewEditCourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        fragment.setArguments(args);
        return fragment;
    }

    public static NewEditCourseFragment editCourseInstance(UUID semesterId, UUID courseId) {
        // change this later this is just so we dont have return error
        NewEditCourseFragment fragment = new NewEditCourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }
}
