package com.example.konrad.coursehub;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
public class SemesterListFragment extends ListFragment {

    private static final String DIALOG_NEW_SEMESTER = "new semester";
    private static final String DIALOG_EDIT_SEMESTER = "edit semster";
    private static final int REQUEST_NEW_SEMESTER = 0;
    private static final int REQUEST_EDIT_SEMESTER = 1;

    private OnFragmentInteractionListener mListener;
    SemesterAdapter mAdapter;
    Button mAddSemesterButton;

    private final String TAG = "SemesterListFragment: ";


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SemesterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //TEMPORARY FIX REMOVE THIS CODE AND FIX LATER
        ArrayList<Semester> semesters = Academics.get(getActivity()).getSemesters();
        for (Semester s: semesters) {
            for (Course c: s.getCourses()){
                c.setCourseEvents(new ArrayList<CourseEvent>());
                for (GradeCategory g: c.getGradeCategories()) {
                    g.setCourseEvents(new ArrayList<CourseEvent>());
                }
            }
        }
        // END OF TEMPORARY FIX
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new SemesterAdapter(Academics.get(getActivity()).getSemesters());
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = (View)inflater.inflate(R.layout.fragment_semesters_page, null);
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
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_edit_semester:
                        int length = lv.getCount();
                        SparseBooleanArray checked = lv.getCheckedItemPositions();
                        Semester semesterToEdit = new Semester();
                        // only allow editing if length is 1
                        int numChecked = 0;
                        for (int i = 0; i < length; i++) {
                            if (checked.get(i)) {
                                // this is a selected semester
                                semesterToEdit = mAdapter.getItem(i);
                                numChecked++;
                            }
                        }
                        // only allow editing if length is 1
                        if (numChecked == 1) {
                        FragmentManager fm = getFragmentManager();
                        NewSemesterFragment dialog = NewSemesterFragment.editSemesterInstance(semesterToEdit.getId(),
                                semesterToEdit.getSeason(), semesterToEdit.getYear());
                        dialog.setTargetFragment(SemesterListFragment.this, REQUEST_EDIT_SEMESTER);
                        dialog.show(fm, DIALOG_EDIT_SEMESTER);
                        actionMode.finish();
                        return true; }
                        else {
                            return false;
                        }
                    case R.id.menu_item_remove_semester:
                    default:
                        return false;
                }

            }
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        mAddSemesterButton = (Button)v.findViewById(R.id.addSemesterButton);
        mAddSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSemesterFragment dialog = new NewSemesterFragment();
                FragmentManager fm = getActivity().getFragmentManager();
                dialog.setTargetFragment(SemesterListFragment.this, REQUEST_NEW_SEMESTER);
                dialog.show(fm, DIALOG_NEW_SEMESTER);
            }
        });
        return v;
    }

    public class SemesterAdapter extends ArrayAdapter<Semester> {
        public SemesterAdapter(ArrayList<Semester> semesters) {
            super(getActivity(), 0, semesters);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_semester_course, null);
            }
            Semester semester = getItem(position);

            TextView seasonTextView = (TextView)convertView.findViewById(R.id.semesterCourseTextView);
            seasonTextView.setText(semester.getSeason() + " " + semester.getYear());
            return convertView;
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Semester s = mAdapter.getItem(position);
        CourseListFragment fragment = CourseListFragment.newInstance(s.getId());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack("fragBack");
        transaction.commit();
        /*super.onListItemClick(l, v, position, id);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // new semester created
        if (requestCode == REQUEST_NEW_SEMESTER) {
            String season = intent.getStringExtra(NewSemesterFragment.EXTRA_SEASON);
            int year = intent.getIntExtra(NewSemesterFragment.EXTRA_YEAR, 2012);
            Semester newSemester = new Semester();
            newSemester.setSeason(season);
            newSemester.setYear(year);
            Academics.get(getActivity()).getSemesters().add(newSemester);
            mAdapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_EDIT_SEMESTER) {
            String season = intent.getStringExtra(NewSemesterFragment.EXTRA_SEASON);
            int year = intent.getIntExtra(NewSemesterFragment.EXTRA_YEAR, 2012);
            UUID semesterId = (UUID)intent.getSerializableExtra(NewSemesterFragment.EXTRA_SEMESTER_ID);
            Semester semesterToEdit = new Semester();
            for (Semester s: Academics.get(getActivity()).getSemesters()) {
                if (s.getId() == semesterId) {
                    semesterToEdit = s;
                    Log.i(TAG, "correct semester found");
                }
            }
            semesterToEdit.setSeason(season);
            semesterToEdit.setYear(year);
            Log.i(TAG, "correct data set");
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_fragment_semester_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_item_new_semester:
                NewSemesterFragment dialog = new NewSemesterFragment();
                FragmentManager fm = getActivity().getFragmentManager();
                dialog.setTargetFragment(SemesterListFragment.this, REQUEST_NEW_SEMESTER);
                dialog.show(fm, DIALOG_NEW_SEMESTER);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_semesters_page, menu);
    }
    */
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
