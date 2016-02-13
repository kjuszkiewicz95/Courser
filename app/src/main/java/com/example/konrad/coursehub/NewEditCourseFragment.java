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
    Course mNewCourse;
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

    boolean startTimeChanged = false;
    boolean endTimeChanged = false;

    public NewEditCourseFragment() {
        // Required empty public constructor
        // then eventually we have to handle case of simply editing course
        categoryEditTextList = new HashMap<Integer, String>();
        percentageEditTextList = new HashMap<Integer, Double>();
        courseBreakdownsViewList = new ArrayList<View>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        // if we are adding a new course to an existing semester
        if (arguments != null && (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID) != null && (UUID)arguments.getSerializable(EXTRA_COURSE_ID) != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID)arguments.getSerializable(EXTRA_COURSE_ID);
            Semester semester = new Semester();
            for (Semester s: Academics.get(getActivity()).getSemesters()) {
                if (s.getId() == mSemesterId) {
                    semester = s;
                }
            }
            for (Course c: semester.getCourses()) {
                if (c.getId() == mCourseId) {
                    mNewCourse = c;
                }
            }
        }
        else if (arguments!= null && (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID) != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mNewCourse = new Course();
            Log.i(TAG, "New course has been made.");
        }
        else {
            // do nothing, has to be either creating a new course or editing a current course
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
            titleEditText.setText(mNewCourse.getTitle());
        }
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNewCourse.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mStartTimeButton = (Button)v.findViewById(R.id.startTimeButton);
        if (mCourseId != null) {
            int startHour = mNewCourse.getStartHour();
            int startMinute = mNewCourse.getStartMinute();
            String timeString = timeStringFormatter(startHour, startMinute);
            mStartTimeButton.setText(timeString);
        }
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour;
                int currentMin;
                if (startTimeChanged || mCourseId != null) {
                    currentHour = mNewCourse.getStartHour();
                    currentMin = mNewCourse.getStartMinute();
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
            int endHour = mNewCourse.getEndHour();
            int endMinute = mNewCourse.getEndMinute();
            String timeString = timeStringFormatter(endHour, endMinute);
            mEndTimeButton.setText(timeString);
        }
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentHour;
                int currentMin;
                if (endTimeChanged || mCourseId != null) {
                    currentHour = mNewCourse.getEndHour();
                    currentMin = mNewCourse.getEndMinute();
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
            for (int i = 0; i < mNewCourse.getDays().size(); i++) {
                switch (mNewCourse.getDays().get(i)) {
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
                    mNewCourse.getDays().add("Monday");
                } else {
                    mNewCourse.getDays().remove("Monday");
                }
            }
        });
        tueCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Tuesday");
                }
                else {
                    mNewCourse.getDays().remove("Tuesday");
                }
            }
        });
        wedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Wednesday");
                }
                else {
                    mNewCourse.getDays().remove("Wednesday");
                }
            }
        });
        thurCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Thursday");
                }
                else {
                    mNewCourse.getDays().remove("Thursday");
                }
            }
        });
        friCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Friday");
                }
                else {
                    mNewCourse.getDays().remove("Friday");
                }
            }
        });
        satCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Saturday");
                }
                else {
                    mNewCourse.getDays().remove("Saturday");
                }
            }
        });
        sunCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mNewCourse.getDays().add("Sunday");
                }
                else {
                    mNewCourse.getDays().remove("Sunday");
                }
            }
        });

        //Linear layout which will hold the children linearlayouts (list_item_grade_breadowns);
        courseBreakdownsContainer = (LinearLayout)v.findViewById(R.id.course_breakdowns_container);
        mAddItem = (ImageView)v.findViewById(R.id.add_topic_imageView);

        // in case of editing course we have to add all existing gradeCategories
        if (mCourseId != null && !mNewCourse.getGradeCategories().isEmpty()) {
            // have to add all of the gradecategory views dynamically
            for (GradeCategory g: mNewCourse.getGradeCategories()) {
                final LinearLayout courseBreakdownView = (LinearLayout)inflater.inflate(R.layout.list_item_grade_breakdown, null);
                courseBreakdownsViewList.add(courseBreakdownView);
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
                        mNewCourse.getGradeCategories().get(position).setTitle(editable.toString());
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
                        mNewCourse.getGradeCategories().get(position).setPercentage(Double.parseDouble(editable.toString()));
                    }
                });
                ImageView deleteButton = (ImageView)courseBreakdownView.findViewById(R.id.delete_imageView);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        // remove this gradecategory from the course object
                        mNewCourse.getGradeCategories().remove(position);
                        // remove this gradecategory view from the grade courseBreakdownsViewList
                        courseBreakdownsViewList.remove(position);
                        // finally remove this view entirely from our container
                        courseBreakdownsContainer.removeView(courseBreakdownView);
                    }
                });
                courseBreakdownsContainer.addView(courseBreakdownView);
            }
        }
        // handles the adding of new gradeCategories
        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradeCategory category = new GradeCategory();
                mNewCourse.getGradeCategories().add(category);
                final LinearLayout courseBreakdownView = (LinearLayout)inflater.inflate(R.layout.list_item_grade_breakdown, null);
                courseBreakdownsViewList.add(courseBreakdownView);

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
                        mNewCourse.getGradeCategories().get(position).setTitle(editable.toString());
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
                            mNewCourse.getGradeCategories().get(position).setPercentage(Double.parseDouble(editable.toString()));
                        }
                    }
                });
                ImageView deleteButton = (ImageView)courseBreakdownView.findViewById(R.id.delete_imageView);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // FIX THIS CODE SO MUCH OF THIS UNNECESSARY
                        int position = courseBreakdownsViewList.indexOf(courseBreakdownView);
                        mNewCourse.getGradeCategories().remove(position);
                        View courseBreakdownViewToDelete = courseBreakdownsViewList.get(position);
                        courseBreakdownsContainer.removeView(courseBreakdownViewToDelete);
                        courseBreakdownsViewList.remove(position);
                    }
                });
                courseBreakdownsContainer.addView(courseBreakdownView);
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
                            s.getCourses().add(mNewCourse);
                        }
                    }
                    // in the case of new course we do not add this fragment to backstack
                    // we want to go from here --> single course view --> all courses view --> all semesters view
//                    Academics.get(getActivity()).saveSemesters();
//                    FragmentManager fm = getFragmentManager();
//                    fm.popBackStack();
//                    FragmentTransaction transaction = fm.beginTransaction();
//                    CourseFragment fragment = CourseFragment.newInstance(mSemesterId, mNewCourse.getId());
//                    transaction.replace(R.id.fragmentContainer, fragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
                    // from fragment 2 I go back to fragment 1 and then straight to fragment 3. This way the back button will bring me again from 3 to 1
                    Academics.get(getActivity()).saveSemesters();
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack();
                    Intent i = new Intent(getActivity(), CoursePagerActivity.class);
                    i.putExtra(CoursePagerActivity.SELECTED_SEMESTER_ID, mSemesterId);
                    i.putExtra(CoursePagerActivity.SELECTED_COURSE_ID, mNewCourse.getId());
                    startActivity(i);
                }
                // if we were editing a course, simply navigate back to courses list of that semester
                else {
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
        for (GradeCategory g: mNewCourse.getGradeCategories()) {
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
            mNewCourse.setStartHour(hour);
            mNewCourse.setStartMinute(minute);
            mStartTimeButton.setText(timeStringFormatter(hour, minute));
            startTimeChanged = true;
        }
        if (requestCode == REQUEST_END_TIME) {
            mNewCourse.setEndHour(hour);
            mNewCourse.setEndMinute(minute);
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
