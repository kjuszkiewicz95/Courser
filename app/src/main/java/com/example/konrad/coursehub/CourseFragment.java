package com.example.konrad.coursehub;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.konrad.coursehub.dummy.DummyContent;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class CourseFragment extends ListFragment {
    public static final String EXTRA_SEMESTER_ID = "com.example.konrad.coursehub.semesterId";
    public static final String EXTRA_COURSE_ID = "com.example.konrad.coursehub.courseId";
    private OnFragmentInteractionListener mListener;
    TextView mUpcomingTextView;
    Button addEventButton;
    UUID mSemesterId;
    UUID mCourseId;
    Semester mSemester;
    Course mCourse;
    UpcomingEventAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mSemesterId = (UUID)arguments.getSerializable(EXTRA_SEMESTER_ID);
            mCourseId = (UUID)arguments.getSerializable(EXTRA_COURSE_ID);
        }
        // find semester
        for (Semester s: Academics.get(getActivity()).getSemesters()) {
            if (s.getId().equals(mSemesterId)) {
                mSemester = s;
            }
        }
        // after finding semester find course
        for (Course c: mSemester.getCourses()) {
            if(c.getId().equals(mCourseId)) {
                mCourse = c;
            }
        }
        adapter = new UpcomingEventAdapter(new ArrayList<UpcomingEvent>());
        setListAdapter(adapter);
    }

    public class UpcomingEventAdapter extends ArrayAdapter<UpcomingEvent> {
        public UpcomingEventAdapter(ArrayList<UpcomingEvent> upcomingEvents) {
            super(getActivity(), 0, upcomingEvents);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_event_small, null);
            }
            return convertView;
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_course_main, null);
        mUpcomingTextView = (TextView) v.findViewById(R.id.upcomingEventsTextView);
        mUpcomingTextView.setText(mCourse.getTitle() + " UPCOMING EVENTS");
        addEventButton = (Button) v.findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fm = getChildFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                NewUnspecifiedEventFragment fragment = new NewUnspecifiedEventFragment();
//                transaction.replace(R.id.fragmentUpcomingPassedContainer, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                // didn't work will try with activity
                Intent i = new Intent(getActivity(), NewUnspecifiedEventActivity.class);
                i.putExtra(NewUnspecifiedEventActivity.EXTRA_SEMESTER_ID, mSemesterId);
                i.putExtra(NewUnspecifiedEventActivity.EXTRA_COURSE_ID, mCourseId);
                startActivity(i);

            }
        });
        return v;
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
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
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

    public static CourseFragment newInstance(UUID semesterId, UUID courseId) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SEMESTER_ID, semesterId);
        args.putSerializable(EXTRA_COURSE_ID, courseId);
        fragment.setArguments(args);
        return fragment;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
