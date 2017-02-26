package com.android.falldetector;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Wave#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Wave extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private int mSectionNumber;

    public Wave() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param sectionNumber section number of this fragment in the host activity.
     * @return A new instance of fragment Wave.
     */
    // TODO: Rename and change types and number of parameters
    public static Wave newInstance(int sectionNumber) {
        Wave fragment = new Wave();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Wave", "------> onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wave, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateView(float ax, float ay, float az) {
        TextView x = (TextView)getView().findViewById(R.id.ax);
        x.setText("ax = " + ax);
        TextView y = (TextView)getView().findViewById(R.id.ay);
        y.setText("ay = " + ay);
        TextView z = (TextView)getView().findViewById(R.id.az);
        z.setText("az = " + az);
    }
}
