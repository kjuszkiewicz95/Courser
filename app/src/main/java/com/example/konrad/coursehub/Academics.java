package com.example.konrad.coursehub;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Konrad on 8/14/2015.
 */
public class Academics {
    private static final String TAG = "Academics";
    private static final String FILENAME = "semesters.json";

    private static Academics sAcademics;
    private ArrayList<Semester> mSemesters;
    private CourseHubJSONSerializer mSerializer;
    private Context mAppContext;


    private Academics(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CourseHubJSONSerializer(mAppContext, FILENAME);
        try {
            mSemesters = mSerializer.loadSemesters();
            Log.d(TAG, "SUCCESS loading semesters!!!");
        } catch (Exception e) {
            mSemesters = new ArrayList<Semester>();
            Log.d(TAG, "Error loading semesters: ", e);
        }
    }

    public static Academics get(Context c) {
        if (sAcademics == null) {
            sAcademics = new Academics(c.getApplicationContext());
        }
        return sAcademics;
    }

    public ArrayList<Semester> getSemesters() {
        return mSemesters;
    }

    public boolean saveSemesters() {
        try {
            mSerializer.saveSemeseters(mSemesters);
            Log.d(TAG, "SUCCESS: Semesters saved to file");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "ERROR: Semesters did not save to file: ", e);
            return false;
        }
    }

}
