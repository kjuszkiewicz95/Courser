package com.example.konrad.coursehub;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Konrad on 11/15/2015.
 */
public class CourseHubJSONSerializer {

    private Context mContext;
    private String mFileName;

    public CourseHubJSONSerializer (Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public void saveSemeseters(ArrayList<Semester> semesters) throws JSONException, IOException {
        //BUILD AN ARRAY IN JSON
        JSONArray array = new JSONArray();
        for (Semester s: semesters) {
            array.put(s.toJSON());
        }
        /// WRITE THE FILE TO DISK
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Semester> loadSemesters() throws IOException, JSONException {
        //
        ArrayList<Semester> semesters = new ArrayList<Semester>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            // PARSE THE STRING USING JSONTOKENER
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                semesters.add(new Semester(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // happens when starting fresh
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return semesters;
    }
}
