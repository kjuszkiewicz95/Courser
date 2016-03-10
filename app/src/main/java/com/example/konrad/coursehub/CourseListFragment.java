package com.example.konrad.coursehub;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class CourseListFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    private static final int REQUEST_EDIT_SEMESTER = 1;

    UUID mSemesterId;
    CourseAdapter mAdapter;
    Semester mSemester;
    Button mAddCourseButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle arguments = getArguments();
        if (arguments != null && (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID) != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
        }
        for (Semester s: Academics.get(getActivity()).getSemesters()) {
            if (s.getId() == mSemesterId) {
                mSemester = s;
            }
        }
        mAdapter = new CourseAdapter(mSemester.getCourses());
        setListAdapter(mAdapter);
    }


    public class CourseAdapter extends ArrayAdapter<Course> {
        public CourseAdapter(ArrayList<Course> courses) {
            super(getActivity(), 0, courses);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_semester_course, null);
            }
            Course course = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.semesterCourseTextView);
            titleTextView.setText(course.getTitle());
            return convertView;
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_courses_page, null);
        final ListView lv = (ListView)v.findViewById(android.R.id.list);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.context_menu_semesters_page, menu);
                return true;
            }
            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                int length = lv.getCount();
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_edit_semester:
                        SparseBooleanArray checkedEdit = lv.getCheckedItemPositions();
                        Course courseToEdit = new Course();
                        int count = 0;
                        for (int i = 0; i < length; i++) {
                            if (checkedEdit.get(i) == true) {
                                count++;
                                courseToEdit = mAdapter.getItem(i);
                            }
                        }
                        // we can only edit one course at a time
                        if (count == 1) {
                            NewEditCourseFragment fragment = NewEditCourseFragment.editCourseInstance(mSemesterId, courseToEdit.getId());
                            FragmentManager fm = getFragmentManager();
                            FragmentTransaction transaction = fm.beginTransaction();
                            transaction.replace(R.id.fragmentContainer, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                            actionMode.finish();
                        }
                        return true;
                    case R.id.menu_item_remove_semester:
                        SparseBooleanArray checkedDelete = lv.getCheckedItemPositions();
                        ArrayList<Integer> positionsToDelete = new ArrayList<Integer>();
                        for (int j = 0; j < length; j++) {
                            if(checkedDelete.get(j) == true) {
                                positionsToDelete.add(j);
                            }
                        }
                        // After we figured out which courses in the list we want to delete, let's delete them
                        int decreasingListIndexCorrection = 0;
                        for (int position: positionsToDelete) {
                            // the first time we remove we have no problems
                            if (mSemester.getCourses().size() == length) {
                                mSemester.getCourses().remove(position);
                                decreasingListIndexCorrection++;
                            }
                            // not the first time removing, list is smaller than when indices were originally calculated
                            else {
                                mSemester.getCourses().remove(position - decreasingListIndexCorrection);
                                decreasingListIndexCorrection++;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        Academics.get(getActivity()).saveSemesters();
                        return true;
                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });
        mAddCourseButton = (Button)v.findViewById(R.id.addCourseButton);
        mAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                NewEditCourseFragment fragment = NewEditCourseFragment.newCourseInstance(mSemesterId);
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_fragment_course_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_course:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                NewEditCourseFragment fragment = NewEditCourseFragment.newCourseInstance(mSemesterId);
                transaction.replace(R.id.fragmentContainer, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    /*
    @Override

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // PREVIOUSLY WORKING CODE
//        Course selectedCourse = mSemester.getCourses().get(position);
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        CourseFragment fragment = CourseFragment.newInstance(mSemesterId, selectedCourse.getId());
//        transaction.replace(R.id.fragmentContainer, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();

        // SECOND PREVIOUSLY WORKING CODE, VIEWPAGER WORKS BUT NO INDICATOR

//        Course selectedCourse = mSemester.getCourses().get(position);
//        Intent i = new Intent(getActivity(), CoursePagerActivity2.class);
//        i.putExtra(CoursePagerActivity2.SELECTED_SEMESTER_ID, mSemesterId);
//        i.putExtra(CoursePagerActivity2.SELECTED_COURSE_ID, selectedCourse.getId());
//        startActivity(i);

        Course selectedCourse = mSemester.getCourses().get(position);
        Intent i = new Intent(getActivity(), CoursePagerActivity.class);
        i.putExtra(CoursePagerActivity.SELECTED_SEMESTER_ID, mSemesterId);
        i.putExtra(CoursePagerActivity.SELECTED_COURSE_ID, selectedCourse.getId());
        startActivity(i);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public static CourseListFragment newInstance(UUID semesterId) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
