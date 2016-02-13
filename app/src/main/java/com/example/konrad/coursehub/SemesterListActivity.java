package com.example.konrad.coursehub;

import android.app.Fragment;

/**
 * Created by Konrad on 10/31/2015.
 */
public class SemesterListActivity extends SingleFragmentActivity implements SemesterListFragment.OnFragmentInteractionListener {
    @Override
    public Fragment createFragment() {
        return new SemesterListFragment();
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
    @Override
    public void onFragmentInteraction(String id) {

    }
}
