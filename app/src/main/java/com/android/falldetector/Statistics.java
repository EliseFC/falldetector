package com.android.falldetector;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {

    private HistoryDBHelper mDbHelper;

    public Statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Statistics.
     */
    public static Statistics newInstance(HistoryDBHelper dbhelper) {
        Statistics fragment = new Statistics();
        fragment.mDbHelper = dbhelper;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_statistics, container, false);
        updateView(v, mDbHelper);
        return v;
    }

    void updateView(View v, HistoryDBHelper dbHelper) {
        TextView tvTotalFalls = (TextView) v.findViewById(R.id.total_fall_value);
        tvTotalFalls.setText(String.valueOf(dbHelper.getCount()));
        TextView tvTruePositive = (TextView) v.findViewById(R.id.true_positive_value);
        tvTruePositive.setText(String.valueOf(dbHelper.getTrueFallsCount()));
        TextView tvFalsePositive = (TextView) v.findViewById(R.id.false_positive_value);
        tvFalsePositive.setText(String.valueOf(dbHelper.getFalseFallsCount()));
    }

    void updateView(HistoryDBHelper dbHelper) {
        updateView(getView(), dbHelper);
    }
}
