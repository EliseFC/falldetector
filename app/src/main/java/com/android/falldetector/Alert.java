package com.android.falldetector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Alert#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Alert extends Fragment {

    public Alert() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Alert.
     */
    public static Alert newInstance() {
        Alert fragment = new Alert();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Alert", "------> onCreateView");
        View v = inflater.inflate(R.layout.fragment_alert, container, false);

        TextView titleSettings = (TextView) v.findViewById(R.id.textFallDetectionStatus);
        Button buttonSettings = (Button) v.findViewById(R.id.buttonSettings);
        return v;
    }
}
